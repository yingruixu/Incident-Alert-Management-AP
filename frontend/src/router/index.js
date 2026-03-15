import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import Incidents from '../views/Incidents.vue'
import Alerts from '../views/Alerts.vue'
import Monitoring from '../views/Monitoring.vue'
import Settings from '../views/Settings.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/incidents', name: 'Incidents', component: Incidents },
  { path: '/alerts', name: 'Alerts', component: Alerts },
  { path: '/monitoring', name: 'Monitoring', component: Monitoring },
  { path: '/settings', name: 'Settings', component: Settings }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫 - 检查登录
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 如果访问的不是登录页，且没有token，跳转登录
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router