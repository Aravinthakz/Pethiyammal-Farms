export async function api(path, options = {}) {
  const token = localStorage.getItem('rmsvg-token')
  const headers = { ...(options.headers || {}) }
  if (!(options.body instanceof FormData)) {
    headers['Content-Type'] = 'application/json'
  }
  if (token) headers.Authorization = `Bearer ${token}`
  const res = await fetch(path, { ...options, headers })
  if (res.status === 401 && path.startsWith('/api/admin')) {
    localStorage.removeItem('rmsvg-token')
    window.location.href = '/admin/login'
  }
  if (res.status === 204) return null
  const data = await res.json().catch(() => ({}))
  if (!res.ok) throw new Error(data.error || 'Request failed')
  return data
}

export const rupee = (n) =>
  new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(n || 0)

export function waLink(number, text) {
  const n = (number || '').replace(/[^\d]/g, '')
  return `https://wa.me/${n}?text=${encodeURIComponent(text)}`
}

const DEFAULT_LIVESTOCK_IMAGES = {
  CHICKEN: '/chicken.jpg',
  GOAT: '/goat.jpg',
  COW: '/cow.jpg',
}

export function defaultLivestockImage(category) {
  return DEFAULT_LIVESTOCK_IMAGES[category] || '/pethiyammal-farms-logo.png'
}

export function livestockWhatsAppText(item, extra = '') {
  const lines = [
    'Hello Pethiyammal Farms,',
    '',
    'I am interested in:',
    '',
    `Product: ${item.title || item.breed}`,
    `Animal ID: ${item.animalCode}`,
  ]
  if (item.weightKg) lines.push(`Weight: ${item.weightKg} KG`)
  if (item.pricingType === 'PER_KG') {
    lines.push(`Price: ₹${item.price} / KG`)
  } else {
    lines.push(`Price: ₹${item.price}`)
  }
  if (extra) lines.push(extra)
  lines.push('', 'Please confirm availability.')
  return lines.join('\n')
}

const LIST_KEY = 'rmsvg-enquiry-list'

export function getEnquiryList() {
  try {
    return JSON.parse(localStorage.getItem(LIST_KEY) || '[]')
  } catch {
    return []
  }
}

export function toggleEnquiryList(item) {
  const list = getEnquiryList()
  const exists = list.find((x) => x.id === item.id)
  const next = exists ? list.filter((x) => x.id !== item.id) : [...list, { id: item.id, title: item.title, animalCode: item.animalCode }]
  localStorage.setItem(LIST_KEY, JSON.stringify(next))
  return next
}

export function inEnquiryList(id) {
  return getEnquiryList().some((x) => x.id === id)
}
