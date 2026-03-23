const EARTH_RADIUS = 6378137

function toRad(deg) {
  return (deg * Math.PI) / 180
}

function calculateDistance(prev, next) {
  if (!prev || !next) return 0
  const radLat1 = toRad(prev.latitude)
  const radLat2 = toRad(next.latitude)
  const a = radLat1 - radLat2
  const b = toRad(prev.longitude) - toRad(next.longitude)
  let distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)))
  distance *= EARTH_RADIUS
  return Math.max(distance, 0)
}

function formatDuration(seconds) {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${pad(mins)}:${pad(secs)}`
}

function formatPace(distanceMeters, seconds) {
  if (!distanceMeters) return '0' 
  const pace = seconds / 60 / (distanceMeters / 1000)
  const mins = Math.floor(pace)
  const secs = Math.round((pace - mins) * 60)
  return `${pad(mins)}'${pad(secs)}"`
}

function pad(val) {
  return val < 10 ? `0${val}` : `${val}`
}

module.exports = {
  calculateDistance,
  formatDuration,
  formatPace
}
