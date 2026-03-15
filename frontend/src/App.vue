<template>
  <div id="app">
    <!-- 侧边栏 -->
    <el-container style="height: 100vh">
      <el-aside width="220px" style="background: #304156">
        <div class="logo">
          <h2>🎯 Incident Manager</h2>
        </div>

        <el-menu
          :default-active="$route.path"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/dashboard">
            <span>📊 Dashboard</span>
          </el-menu-item>
          <el-menu-item index="/incidents">
            <span>🚨 Incidents</span>
          </el-menu-item>
          <el-menu-item index="/alerts">
            <span>🔔 Alerts</span>
          </el-menu-item>
          <el-menu-item index="/monitoring">
            <span>💻 Monitoring</span>
          </el-menu-item>
          <el-menu-item index="/settings">
            <span>⚙️ Settings</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容 -->
      <el-container>
        <el-header style="background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,.08)">
          <div style="display: flex; justify-content: space-between; align-items: center; height: 100%">
            <h3>Incident Management System</h3>
            <div>
              <span>👤 Admin</span>
            </div>
          </div>
        </el-header>

        <el-main style="background: #f0f2f5">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup>
import { useRoute } from 'vue-router'
const $route = useRoute()
</script>
<style>
* {
  margin: 0;
  padding: 0;
}
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #2b3a4a;
}
.logo h2 {
  color: #fff;
  font-size: 18px;
}
.el-menu-item {
  height: 50px;
  line-height: 50px;
}
.el-menu-item span {
  margin-left: 8px;
}
</style>