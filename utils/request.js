const DEFAULT_BASE_URL = 'https://springboot-ph0l-237202-6-1414422329.sh.run.tcloudbase.com'

let baseUrl = wx.getStorageSync('apiBaseUrl') || DEFAULT_BASE_URL
const cloudConfig = {
  enabled: true,
  envId: 'prod-8ghzw3w4d031ede9',
  serviceName: 'springboot-ph0l'
}
let cloudInitialized = false

function setBaseUrl(url) {
  baseUrl = url || DEFAULT_BASE_URL
  wx.setStorageSync('apiBaseUrl', baseUrl)
}

function setCloudConfig(config = {}) {
  Object.assign(cloudConfig, config)
  if (cloudConfig.envId && wx.cloud && typeof wx.cloud.init === 'function') {
    wx.cloud.init({ env: cloudConfig.envId })
    cloudInitialized = true
  }
}

function ensureCloudInit() {
  if (!cloudConfig.enabled) {
    return
  }
  if (cloudInitialized) {
    return
  }
  if (wx.cloud && typeof wx.cloud.init === 'function' && cloudConfig.envId) {
    wx.cloud.init({ env: cloudConfig.envId })
    cloudInitialized = true
  }
}

function withAuthHeader(header = {}, auth = true) {
  const finalHeader = Object.assign({ 'Content-Type': 'application/json' }, header)
  if (auth) {
    const token = wx.getStorageSync('sessionToken')
    if (token) {
      finalHeader['X-Session-Token'] = token
    }
  }
  return finalHeader
}

function request(options) {
  const { url, method = 'GET', data = {}, header = {}, auth = true } = options
  if (cloudConfig.enabled && wx.cloud && typeof wx.cloud.callContainer === 'function') {
    ensureCloudInit()
    return requestViaCloud({ url, method, data, header, auth })
  }
  return requestViaHttp({ url, method, data, header, auth })
}

function requestViaCloud({ url, method, data, header, auth }) {
  return new Promise((resolve, reject) => {
    wx.cloud.callContainer({
      config: { env: cloudConfig.envId },
      path: url,
      method,
      header: Object.assign(withAuthHeader(header, auth), {
        'X-WX-SERVICE': cloudConfig.serviceName
      }),
      data,
      success: ({ statusCode, data: resp }) => {
        if (statusCode >= 200 && statusCode < 300) {
          resolve(resp)
        } else {
          reject(resp)
        }
      },
      fail: reject
    })
  })
}

function requestViaHttp({ url, method, data, header, auth }) {
  const finalHeader = withAuthHeader(header, auth)
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${baseUrl}${url}`,
      method,
      data,
      header: finalHeader,
      success: ({ data: resp, statusCode }) => {
        if (statusCode >= 200 && statusCode < 300) {
          resolve(resp)
        } else {
          reject(resp)
        }
      },
      fail: reject
    })
  })
}

module.exports = {
  request,
  setBaseUrl,
  setCloudConfig
}
