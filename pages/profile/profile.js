const api = require('../../services/api')

Page({
  data: {
    profile: null,
    saving: false,
    hasSession: false,
    loginLoading: false
  },
  onShow() {
    const app = getApp()
    const hasSession = !!app.globalData.sessionToken
    this.setData({ hasSession })
    if (!hasSession) {
      this.setData({ profile: null })
    }
    if (hasSession) {
      this.loadProfile()
    }
  },
  loadProfile() {
    if (!getApp().globalData.sessionToken) {
      return
    }
    const app = getApp()
    const cached = app.globalData.userProfile
    if (cached) {
      this.setData({ profile: { ...cached } })
    }
    api.fetchProfile().then(profile => {
      this.setData({ profile })
      app.setProfile(profile)
    }).catch(() => {})
  },
  handleInput(e) {
    const { field } = e.currentTarget.dataset
    const value = e.detail.value
    this.setData({ [`profile.${field}`]: value })
  },
  handleSwitch(e) {
    this.setData({ 'profile.voiceEnabled': e.detail.value })
  },
  handleAvatar(e) {
    const { avatarUrl } = e.detail
    this.setData({ 'profile.avatarUrl': avatarUrl })
  },
  handleSave() {
    if (!this.data.hasSession || !this.data.profile) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    this.setData({ saving: true })
    api.updateProfile(this.data.profile).then(profile => {
      wx.showToast({ title: '已保存', icon: 'success' })
      getApp().setProfile(profile)
      this.setData({ saving: false })
    }).catch(() => {
      this.setData({ saving: false })
      wx.showToast({ title: '保存失败', icon: 'none' })
    })
  },
  handleLogin() {
    if (this.data.loginLoading) return
    this.setData({ loginLoading: true })
    wx.getUserProfile({
      desc: '用于同步头像昵称',
      success: ({ userInfo }) => {
        getApp().loginWithUserInfo(userInfo).then(() => {
          wx.showToast({ title: '登录成功', icon: 'success' })
          this.setData({ hasSession: true, loginLoading: false })
          this.loadProfile()
        }).catch(err => {
          console.error('[auth] 个人中心登录失败', err)
          this.setData({ loginLoading: false })
          wx.showToast({ title: '登录失败', icon: 'none' })
        })
      },
      fail: (err) => {
        console.error('[auth] 用户拒绝授权', err)
        this.setData({ loginLoading: false })
        wx.showToast({ title: '需要授权', icon: 'none' })
      }
    })
  },
  handleLogout() {
    getApp().logout()
    this.setData({ profile: null, hasSession: false })
    wx.showToast({ title: '已退出', icon: 'success' })
  },
  handleRelogin() {
    wx.getUserProfile({
      desc: '用于完善资料',
      success: ({ userInfo }) => {
        getApp().loginWithUserInfo(userInfo).then(() => {
          wx.showToast({ title: '已更新头像昵称', icon: 'success' })
          this.loadProfile()
        }).catch(err => {
          console.error('[auth] 重新登录失败', err)
          wx.showToast({ title: '重新登录失败', icon: 'none' })
        })
      }
    })
  }
})
