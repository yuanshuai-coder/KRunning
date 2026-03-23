const api = require('../../services/api')

const samplePolyline = [
  { latitude: 23.13455, longitude: 113.3225 },
  { latitude: 23.1349, longitude: 113.3239 },
  { latitude: 23.1357, longitude: 113.3245 },
  { latitude: 23.1364, longitude: 113.3238 },
  { latitude: 23.1361, longitude: 113.3224 }
]

function formatDistance(meters = 0, withUnit = true) {
  const km = ((meters || 0) / 1000).toFixed(2)
  return withUnit ? `${km} km` : km
}

Page({
  data: {
    hasSession: false,
    showLoginGuide: false,
    loginLoading: false,
    locationReady: false,
    mapScale: 17,
    mapCenter: { latitude: 23.13455, longitude: 113.3225 },
    polyline: [
      {
        points: samplePolyline,
        color: '#0F9D58',
        width: 6
      }
    ],
    leaderboardScope: 'DAY',
    summary: null,
    leaderboard: [],
    loading: false,
    modes: [
      { id: 'FREE', label: '自由跑', desc: '不设目标，想停就停', mode: 'FREE', target: '' },
      { id: 'TARGET2', label: '目标跑', desc: '完成 2 公里', mode: 'TARGET', target: 2000 },
      { id: 'COURSE3', label: '课程跑', desc: '课程要求 3 公里', mode: 'COURSE', target: 3000 }
    ],
    activeMode: 'FREE'
  },
  onLoad() {
    this.app = getApp()
    const hasSession = !!this.app.globalData.sessionToken
    this.setData({
      hasSession,
      showLoginGuide: !hasSession
    })
    this.app.onSessionReady(() => {
      this.setData({ hasSession: true, showLoginGuide: false })
      this.loadHomeData()
    })
    if (this.data.hasSession) {
      this.loadHomeData()
    }
    this.prepareLocation()
  },
  onShow() {
    const hasSession = !!this.app.globalData.sessionToken
    this.setData({ hasSession, showLoginGuide: !hasSession })
    if (hasSession) {
      this.loadHomeData()
    }
  },
  onPullDownRefresh() {
    this.loadHomeData()
  },
  prepareLocation() {
    wx.getLocation({
      type: 'gcj02',
      success: ({ latitude, longitude }) => {
        this.setData({
          mapCenter: { latitude, longitude },
          locationReady: true
        })
      },
      fail: () => wx.showToast({ title: '需要定位权限', icon: 'none' })
    })
  },
  loadHomeData() {
    if (!this.data.hasSession) {
      this.setData({ showLoginGuide: true })
      wx.stopPullDownRefresh()
      return
    }
    this.setData({ loading: true })
    Promise.all([
      api.fetchSummary(),
      api.fetchLeaderboard(this.data.leaderboardScope)
    ]).then(([summary, leaderboard]) => {
      const formattedSummary = summary ? Object.assign({}, summary, {
        todayDistanceText: formatDistance(summary.todayDistanceMeters),
        weekDistanceText: formatDistance(summary.weekDistanceMeters),
        monthDistanceText: formatDistance(summary.monthDistanceMeters)
      }) : null
      const formattedLeaderboard = (leaderboard.entries || []).slice(0, 5).map(entry => Object.assign({}, entry, {
        distanceText: formatDistance(entry.totalDistanceMeters)
      }))
      this.setData({
        summary: formattedSummary,
        leaderboard: formattedLeaderboard,
        loading: false
      })
      wx.stopPullDownRefresh()
    }).catch(() => {
      this.setData({ loading: false })
      wx.stopPullDownRefresh()
      wx.showToast({ title: '加载失败', icon: 'none' })
    })
  },
  changeMode(e) {
    const { id } = e.currentTarget.dataset
    this.setData({ activeMode: id })
  },
  toggleLeaderboardScope() {
    const next = this.data.leaderboardScope === 'DAY' ? 'MONTH' : 'DAY'
    this.setData({ leaderboardScope: next }, () => this.loadHomeData())
  },
  handleGo() {
    if (!this.data.hasSession) {
      this.setData({ showLoginGuide: true })
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    if (!this.data.locationReady) {
      wx.showToast({ title: '请先开启定位', icon: 'none' })
      return
    }
    const selected = this.data.modes.find(item => item.id === this.data.activeMode) || this.data.modes[0]
    wx.navigateTo({
      url: `/pages/run/run?mode=${selected.mode}&target=${selected.target || ''}`
    })
  },
  handleViewHistory() {
    if (!this.data.hasSession) {
      this.setData({ showLoginGuide: true })
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    wx.switchTab({ url: '/pages/history/history' })
  },
  handleLoginTap() {
    if (this.data.loginLoading) return
    this.setData({ loginLoading: true })
    // 通过 getUserProfile 主动拉起微信授权，成功后再向后端换取 session
    wx.getUserProfile({
      desc: '用于完成登录并同步跑步记录',
      success: ({ userInfo }) => {
        getApp().loginWithUserInfo(userInfo).then(() => {
          wx.showToast({ title: '登录成功', icon: 'success' })
          this.setData({ hasSession: true, showLoginGuide: false, loginLoading: false })
          this.loadHomeData()
        }).catch(err => {
          console.error('[auth] 登录失败', err)
          this.setData({ loginLoading: false })
          wx.showToast({ title: '登录失败', icon: 'none' })
        })
      },
      fail: (err) => {
        console.error('[auth] 用户拒绝授权', err)
        this.setData({ loginLoading: false })
        wx.showToast({ title: '需要授权才能登录', icon: 'none' })
      }
    })
  },
  handleCloseLoginGuide() {
    // 允许用户跳过登录继续浏览非敏感信息
    this.setData({ showLoginGuide: false })
  }
})
