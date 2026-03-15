import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',  // 后端地址
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
api.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

// Incidents API
export const incidentsApi = {
  getAll: () => api.get('/incidents'),
  getById: (id) => api.get(`/incidents/${id}`),
  create: (data) => api.post('/incidents', data),
  update: (id, data) => api.put(`/incidents/${id}`, data),
  delete: (id) => api.delete(`/incidents/${id}`)
}

// Alerts API
export const alertsApi = {
  getAll: () => api.get('/alerts'),
  create: (data) => api.post('/alerts', data),
  update: (id, data) => api.put(`/alerts/${id}`, data),
  delete: (id) => api.delete(`/alerts/${id}`)
}

// Auth API
export const authApi = {
  login: (username, password) => api.post('/auth/login', { username, password }),
  logout: () => api.post('/auth/logout')
}

export default api