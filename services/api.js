const { request } = require('../utils/request')

const API = {
  login(payload) {
    return request({ url: '/api/auth/login', method: 'POST', data: payload, auth: false })
  },
  logout() {
    return request({ url: '/api/auth/logout', method: 'POST' })
  },
  fetchProfile() {
    return request({ url: '/api/profile' })
  },
  updateProfile(payload) {
    return request({ url: '/api/profile', method: 'PUT', data: payload })
  },
  fetchSummary() {
    return request({ url: '/api/stats/summary' })
  },
  fetchLeaderboard(scope = 'DAY', school) {
    const data = { scope }
    if (school) {
      data.school = school
    }
    return request({ url: '/api/leaderboard', data })
  },
  createRun(payload) {
    return request({ url: '/api/runs', method: 'POST', data: payload })
  },
  fetchRuns(page = 0, size = 10) {
    return request({ url: '/api/runs', data: { page, size } })
  },
  fetchRunDetail(id) {
    return request({ url: `/api/runs/${id}` })
  }
}

module.exports = API
