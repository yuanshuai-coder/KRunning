const api = require('../../services/api')

function formatDistance(meters = 0) {
  return `${((meters || 0) / 1000).toFixed(2)} km`
}

function formatPace(pace = 0) {
  const value = Number(pace) || 0
  return `${value.toFixed(2)} 分/公里`
}

Page({
  data: {
    summary: null,
    runs: [],
    page: 0,
    size: 10,
   loading: false,
   hasSession: false
    loading: false
  },
   this.app = getApp()
   const hasSession = !!this.app.globalData.sessionToken
   this.setData({ hasSession })
   this.app.onSessionReady(() => {
     this.setData({ hasSession: true })
     this.loadAll(true)
   })
   if (hasSession) {
     this.loadAll(true)
   }
    this.loadAll(true)
  },
   if (this.data.hasSession) {
     this.loadAll(true)
   }
    this.loadAll(true)
  },
   if (this.data.hasSession) {
     this.loadAll(true)
   } else {
     wx.stopPullDownRefresh()
   }
    this.loadAll(true)
  },
   this.loadRuns()
    this.loadRuns()
  },
   if (!this.data.hasSession) {
     wx.stopPullDownRefresh()
     return
   }
   if (reset) {
     this.setData({ runs: [], page: 0, hasNext: true })
      this.setData({ runs: [], page: 0, hasNext: true })
    }
    this.fetchSummary()
    this.loadRuns()
  },
   if (!this.data.hasSession) {
     wx.stopPullDownRefresh()
     return
   }
  fetchSummary() {
    api.fetchSummary().then(summary => {
      const formatted = Object.assign({}, summary, {
        weekDistanceText: formatDistance(summary?.weekDistanceMeters || 0),
        monthDistanceText: formatDistance(summary?.monthDistanceMeters || 0)
      })
      this.setData({ summary: formatted })
      wx.stopPullDownRefresh()
    }).catch(() => wx.stopPullDownRefresh())
  },
   if (!this.data.hasSession || this.data.loading || !this.data.hasNext) {
    if (this.data.loading || !this.data.hasNext) {
      wx.stopPullDownRefresh()
      return
    }
    this.setData({ loading: true })
    api.fetchRuns(this.data.page, this.data.size).then(res => {
      const items = (res.items || []).map(item => Object.assign({}, item, {
  },
  handleGoLogin() {
    wx.switchTab({ url: '/pages/home/home' })
        distanceText: formatDistance(item.distanceMeters || 0),
        paceText: formatPace(item.avgPace)
      }))
      this.setData({
        runs: this.data.runs.concat(items),
        page: res.page + 1,
        hasNext: res.hasNext,
        loading: false
      })
      wx.stopPullDownRefresh()
    }).catch(() => {
      this.setData({ loading: false })
      wx.stopPullDownRefresh()
      wx.showToast({ title: '加载记录失败', icon: 'none' })
    })
  },
  handleRunTap(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({ url: `/pages/result/result?id=${id}` })
  }
})
