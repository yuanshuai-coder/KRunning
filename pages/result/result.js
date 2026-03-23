const api = require('../../services/api')

function formatDistance(meters = 0) {
  return `${((meters || 0) / 1000).toFixed(2)} km`
}

function formatPaceValue(value = 0) {
  const pace = Number(value) || 0
  return `${pace.toFixed(2)} 分/公里`
}

function enhanceSummary(summary) {
  if (!summary) {
    return null
  }
  return Object.assign({}, summary, {
    distanceText: formatDistance(summary.distanceMeters),
    paceText: formatPaceValue(summary.avgPace)
  })
}

function enhanceLeaderboard(entries = []) {
  return (entries || []).slice(0, 3).map(entry => Object.assign({}, entry, {
    distanceText: formatDistance(entry.totalDistanceMeters)
  }))
}

Page({
  data: {
    summary: null,
    detail: null,
    leaderboard: [],
    polyline: []
  },
  onLoad(options) {
    const { id } = options
    if (id) {
      this.loadDetail(id)
    } else {
      const cached = getApp().consumeRunResult()
      if (cached && cached.summary) {
        this.setData({ summary: enhanceSummary(cached.summary), detail: cached })
      }
    }
    this.loadLeaderboard()
  },
  loadDetail(id) {
    api.fetchRunDetail(id).then(res => {
      this.setData({
        summary: enhanceSummary(res.summary),
        detail: res,
        polyline: res.gpsTrack && res.gpsTrack.length ? [{ points: res.gpsTrack, color: '#0F9D58', width: 5 }] : []
      })
    }).catch(() => wx.showToast({ title: '加载失败', icon: 'none' }))
  },
  loadLeaderboard() {
    api.fetchLeaderboard('DAY').then(res => {
      this.setData({ leaderboard: enhanceLeaderboard(res.entries) })
    }).catch(() => {})
  },
  handleBackHome() {
    wx.switchTab({ url: '/pages/home/home' })
  },
  handleViewHistory() {
    wx.switchTab({ url: '/pages/history/history' })
  }
})
