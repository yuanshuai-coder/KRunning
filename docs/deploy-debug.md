# 部署与调试指南

本指南面向需要在本地或测试环境快速跑通“后端 REST API + 微信小程序”链路的开发同学，覆盖环境准备、部署步骤与常见调试技巧。

## 1. 环境准备

### 1.1 后端依赖
- JDK 17 及以上（Spring Boot 3.x 默认要求）
- Maven 3.8+（或使用 Gradle Wrapper，但推荐 Maven）
- MySQL 8.0（若使用其他数据库请同步更新 JPA 配置）
- 可用的微信小程序 `appId/appSecret`（用于服务端通过 `wx.login` code 换取 openId）

### 1.2 小程序依赖
- 微信开发者工具（建议 Canary ≥ 1.06，便于使用真机调试）
- 申请到与后端一致的 `AppID`，并在微信公众平台配置合法 request 域名
- 本仓库代码：`d:\Java_code\newRun`

## 2. 启动后端 REST 服务
1. 在后端工程中配置数据库与微信凭证，例如在 `application.yml` 中设置：
   ```yaml
   spring:
     datasource:
      url: jdbc:mysql://10.14.103.12:3306/run_tracker?characterEncoding=utf8&serverTimezone=Asia/Shanghai
      username: root
      password: 4daGaMPX
   wechat:
     miniapp:
       app-id: <AppID>
       app-secret: <AppSecret>
   ```
2. 初始化数据库 schema（Flyway/liquibase 脚本或手工 `schema.sql`）。
3. 运行 `mvn spring-boot:run` 或 `mvn clean package -DskipTests && java -jar target/*.jar`。
4. 本地模式默认监听 `http://localhost:8080`；若部署到微信云托管，可通过 [utils/request.js](../utils/request.js) 的 `setCloudConfig` 使用 `wx.cloud.callContainer` 直接访问托管服务（默认已指向 `springboot-ph0l` 服务，envId 为 `prod-8ghzw3w4d031ede9`）。无论哪种方式，都需要确保以下接口可被调用（参见 [services/api.js](../services/api.js)）：

   | 接口 | 方法 | 说明 |
   | --- | --- | --- |
   | /api/auth/login | POST | 通过 `wx.login` code 完成会话创建 |
   | /api/profile | GET/PUT | 读取与更新个人资料 |
   | /api/stats/summary | GET | 首页与历史页统计摘要 |
   | /api/leaderboard | GET | 日/月排行榜，支持 `scope=DAY\|MONTH` |
   | /api/runs | GET/POST | 拉取分页历史记录 / 上传跑步数据 |
   | /api/runs/{id} | GET | 查询单条跑步详情 |

5. 若前端首先发起请求会返回 401，请确认登录链路是否完整（`/api/auth/login` 会返回 `sessionToken` 与 profile）。

## 3. 小程序导入与基础配置
1. 打开微信开发者工具 → 导入项目 → 选择 `d:\Java_code\newRun`。
2. 填入与后端一致的 `AppID`，勾选“使用云开发”保持关闭。
3. 如需开启真机调试，记得在“项目设置 → 本地设置”中允许编译 `ES6` 与增强编译。
4. `app.json` 已声明 5 个页面（首页/记录/我的/跑步中/结果），tabBar 只展示首页、记录、我的，其余为非 tab 页。

## 4. API 域名与环境切换
- 默认 HTTP 模式 Base URL 已更新为云托管域名 `https://springboot-ph0l-237202-6-1414422329.sh.run.tcloudbase.com`，如需自定义可继续调用 `setBaseUrl('https://api.example.com')`，该方法会把新地址写入本地缓存。
- 如需完全走微信云托管提供的容器通道，可保持 [utils/request.js](../utils/request.js) 中的 `cloudConfig.enabled = true`（默认已开启），系统会自动初始化 `wx.cloud` 并使用 `wx.cloud.callContainer`，无需再在公众平台配置 request 合法域名。
- 如果你暂时希望回退到普通 HTTP 请求，可调用 `setCloudConfig({ enabled: false })` 或直接把配置改为 `false`。
- 线上发布依旧建议在公众平台后台登记 HTTPS 域名，便于灰度和容灾。

## 5. 调试指南

### 5.1 会话与登录
- `App.onLaunch` 会检查本地 `sessionToken`，缺失时自动调用 `wx.login` 并执行 `api.login`（逻辑见 [app.js](../app.js)）。
- 若需要强制重新登录，可在开发者工具控制台执行：
  ```js
  wx.removeStorageSync('sessionToken')
  wx.removeStorageSync('userProfile')
  getApp().bootstrapSession()
  ```
- 依赖会话的页面（如首页/记录）请优先通过 `getApp().onSessionReady(cb)` 注册回调，避免 token 仍未下发就发起请求。

### 5.2 API 与网络
- 使用开发者工具的 Network 面板观察 `/api/**` 请求，必要时勾选“保留日志”。
- 对于 POST `/api/runs`，在跑步结束页也会缓存最后一次成功上传的数据，方便失败重试。
- 后端若返回 4xx/5xx，可在控制台打印 `err` 并对照服务端日志。

### 5.3 运行流程自检
1. **首页**：确认地图能获得定位，统计卡片成功展示 `summary` 数据，排行榜可切换日/月。
2. **跑步中**：真机调试时打开“允许后台定位”，确认距离、配速实时刷新并能暂停/继续。
3. **结果页**：检查地图轨迹、公里分布与排行榜调用 `/api/leaderboard` 是否成功。
4. **历史记录**：下拉刷新触发 `api.fetchSummary` 与 `api.fetchRuns`；触底自动翻页。
5. **个人中心**：头像/昵称/学校/班级/语音开关更新后调用 `api.updateProfile` 并提示保存成功。

### 5.4 断点与日志
- JavaScript 层：可在开发者工具 Sources 面板设置断点或使用 `debugger` 关键字。
- 后端：建议开启 `spring.jpa.show-sql=true` 或使用 `./mvnw spring-boot:run -Dspring-boot.run.arguments="--logging.level.com.unirun.runner=DEBUG"` 观察请求链路。

## 6. 常见问题排查
- **401 未授权**：确认 `X-Session-Token` 是否随请求发送，可在 `utils/request.js` 里打印 `wx.getStorageSync('sessionToken')` 验证。
- **request 域名不合法**：在开发者工具输入 `wx.getSystemInfoSync().SDKVersion`，若 ≥ 2.10.4 仍提示不合法，说明后台未添加该域名。
- **定位失败**：检查手机是否授予定位权限；如在模拟器请使用“模拟定位”功能。
- **排行榜为空**：`/api/leaderboard` 需要后端计算日/月累计，确认数据库已有跑步记录，否则页面会展示“暂无数据”。

按照以上流程，便可实现“后端 REST 服务 + 微信小程序”从启动到调试的完整闭环。祝开发顺利！
