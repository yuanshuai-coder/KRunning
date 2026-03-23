const api = require('../../services/api')
const { calculateDistance, formatDuration, formatPace } = require('../../utils/geo')

function formatDistanceText(meters = 0) {
  return `${((meters || 0) / 1000).toFixed(2)} km`
}

Page({
  data: {
    state: 'countdown',
    countdown: 3,
    distanceMeters: 0,
    distanceText: '0.00 km',
    durationSeconds: 0,
    durationText: '00:00',
    paceText: '00:00',
    modeLabel: '自由跑',
    targetDistance: 0,
    polyline: [],
    paused: false
  },
  onLoad(options) {
    this.mode = options.mode || 'FREE'
    this.target = Number(options.target) || 0
    this.setData({
      modeLabel: this.getModeLabel(this.mode),
      targetDistance: this.target
    })
    this.countdownTimer = null
    this.ticker = null
    this.track = []
    this.startCountdown()
  },
  onUnload() {
    this.cleanup()
  },
  startCountdown() {
    this.setData({ state: 'countdown', countdown: 3 })
    this.countdownTimer = setInterval(() => {
      if (this.data.countdown <= 1) {
        clearInterval(this.countdownTimer)
        this.beginRun()
      } else {
        this.setData({ countdown: this.data.countdown - 1 })
      }
    }, 1000)
  },
  beginRun() {
    this.startTime = Date.now()
    this.setData({ state: 'running', paused: false })
    this.startTicker()
    this.startLocationStream()
  },
  startTicker() {
    this.ticker = setInterval(() => {
      const duration = Math.floor((Date.now() - this.startTime) / 1000)
      this.setData({
        durationSeconds: duration,
        durationText: formatDuration(duration),
        paceText: formatPace(this.data.distanceMeters, duration)
      })
    }, 1000)
  },
  startLocationStream() {
    wx.startLocationUpdate({
      type: 'gcj02',
      success: () => {
        this.locationListener = (res) => this.handleLocation(res)
        if (wx.onLocationChange) {
          wx.onLocationChange(this.locationListener)
        }
      },
      fail: () => wx.showToast({ title: '无法获取定位', icon: 'none' })
    })
  },
  handleLocation(location) {
    const point = {
      latitude: location.latitude,
      longitude: location.longitude,
      speed: location.speed || 0
    }
    const track = this.track
    let newDistance = this.data.distanceMeters
    if (track.length) {
      const delta = calculateDistance(track[track.length - 1], point)
      newDistance += delta
    }
    track.push(point)
    this.setData({
      distanceMeters: newDistance,
      distanceText: formatDistanceText(newDistance),
      polyline: [{ points: track, color: '#0F9D58', width: 5 }]
    })
  },
  handlePause() {
    if (this.data.state !== 'running') return
    this.setData({ state: 'paused', paused: true })
    this.stopTicker()
    this.stopLocationStream()
  },
  handleResume() {
    if (this.data.state !== 'paused') return
    this.setData({ state: 'running', paused: false })
    this.startTime = Date.now() - this.data.durationSeconds * 1000
    this.startTicker()
    this.startLocationStream()
  },
  handleFinish() {
    this.stopTicker()
    this.stopLocationStream()
    const payload = {
      mode: this.mode,
      targetDistanceMeters: this.target || null,
      distanceMeters: Math.round(this.data.distanceMeters),
      durationSeconds: this.data.durationSeconds,
      calories: null,
      startTime: new Date(this.startTime).toISOString(),
      endTime: new Date().toISOString(),
      gpsTrack: this.track.map(item => ({
        latitude: item.latitude,
        longitude: item.longitude,
        speed: item.speed
      }))
    }
    wx.showLoading({ title: '同步中', mask: true })
    api.createRun(payload).then(res => {
      wx.hideLoading()
      getApp().cacheRunResult({ summary: res })
      wx.redirectTo({ url: `/pages/result/result?id=${res.id}` })
    }).catch(() => {
      wx.hideLoading()
      wx.showToast({ title: '上传失败', icon: 'none' })
    })
  },
  stopTicker() {
    if (this.ticker) {
      clearInterval(this.ticker)
      this.ticker = null
    }
  },
  stopLocationStream() {
    if (this.locationListener && wx.offLocationChange) {
      wx.offLocationChange(this.locationListener)
    }
    wx.stopLocationUpdate()
  },
  cleanup() {
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer)
    }
    this.stopTicker()
    this.stopLocationStream()
  },
  getModeLabel(mode) {
    if (mode === 'TARGET') return '目标跑'
    if (mode === 'COURSE') return '课程跑'
    return '自由跑'
  }
})
