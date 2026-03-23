// app.js
const api = require('./services/api')

const DEFAULT_AVATAR = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

// 将微信返回的性别枚举统一成后端接受的字符串
function normalizeGender(value) {
  if (value === 1 || value === 'male') {
    return 'male'
  }
  if (value === 2 || value === 'female') {
    return 'female'
  }
  return 'unknown'
}

App({
  globalData: {
    sessionToken: wx.getStorageSync('sessionToken') || '',
    userProfile: wx.getStorageSync('userProfile') || null,
    latestRun: null
  },
  sessionReadyQueue: [],
  onLaunch() {
    if (this.globalData.sessionToken) {
      this.flushSessionReady()
    }
  },
  loginWithUserInfo(userInfo, extra = {}) {
    return new Promise((resolve, reject) => {
      wx.login({
        success: ({ code }) => {
          const payload = {
            nickname: userInfo.nickName || extra.nickname || '校园跑者',
            avatarUrl: userInfo.avatarUrl || extra.avatarUrl || DEFAULT_AVATAR,
            school: extra.school ?? (this.globalData.userProfile?.school || ''),
            className: extra.className ?? (this.globalData.userProfile?.className || ''),
            gender: normalizeGender(userInfo.gender),
            country: userInfo.country || '',
            province: userInfo.province || '',
            city: userInfo.city || '',
            clientBuild: 1,
            code
          }
          api.login(payload).then(res => {
            this.setSession(res.sessionToken)
            this.setProfile(res.profile)
            this.flushSessionReady()
            resolve(res)
          }).catch(err => {
            console.error('[auth] 调用后端登录接口失败', err)
            reject(err)
          })
        },
        fail: (err) => {
          console.error('[auth] wx.login 调用失败', err)
          reject(err)
        }
      })
    })
  },
  onSessionReady(cb) {
    if (this.globalData.sessionToken) {
      cb(this.globalData)
    } else {
      this.sessionReadyQueue.push(cb)
    }
  },
  flushSessionReady() {
    while (this.sessionReadyQueue.length) {
      const cb = this.sessionReadyQueue.shift()
      if (typeof cb === 'function') {
        cb(this.globalData)
      }
    }
  },
  setSession(token) {
    this.globalData.sessionToken = token
    wx.setStorageSync('sessionToken', token)
  },
  setProfile(profile) {
    this.globalData.userProfile = profile
    wx.setStorageSync('userProfile', profile)
  },
  clearSession() {
    this.globalData.sessionToken = ''
    this.globalData.userProfile = null
    wx.removeStorageSync('sessionToken')
    wx.removeStorageSync('userProfile')
  },
  // 主动退出登录时清除会话并通知后端注销
  logout() {
    if (this.globalData.sessionToken) {
      api.logout().catch(() => {})
    }
    this.clearSession()
  },
  cacheRunResult(payload) {
    this.globalData.latestRun = payload
    wx.setStorageSync('latestRun', payload)
  },
  consumeRunResult() {
    const payload = this.globalData.latestRun || wx.getStorageSync('latestRun')
    this.globalData.latestRun = null
    wx.removeStorageSync('latestRun')
    return payload
  }
})
