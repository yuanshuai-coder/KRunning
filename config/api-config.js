module.exports = {
  // 云托管公网域名，HTTP 请求会直接拼接此地址
  baseUrl: 'https://springboot-ph0l-237202-6-1414422329.sh.run.tcloudbase.com',
  // 微信云托管调用配置；若希望改走普通 HTTPS，可把 enabled 设为 false
  cloud: {
    enabled: true,
    envId: 'prod-8ghzw3w4d031ede9',
    serviceName: 'springboot-ph0l'
  }
}
