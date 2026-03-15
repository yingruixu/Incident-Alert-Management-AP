<template>
  <div class="monitoring">
    <!-- 服务列表 -->
    <el-card class="toolbar">
      <el-row :gutter="20">
        <el-col :span="12">
          <h3>🏗️ Infrastructure</h3>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <el-button type="primary" size="small" @click="refreshData">
            🔄 Refresh
          </el-button>
        </el-col>
      </el-row>
    </el-card>
    <!-- 服务卡片网格 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="8" v-for="server in servers" :key="server.name">
        <el-card class="server-card">
          <template #header>
            <div class="server-header">
              <span class="server-name">{{ server.name }}</span>
              <el-tag :type="server.status === 'healthy' ? 'success' : 'danger'" size="small">
                {{ server.status }}
              </el-tag>
            </div>
          </template>

          <!-- CPU -->
          <div class="metric">
            <div class="metric-label">
              <span>CPU</span>
              <span>{{ server.cpu }}%</span>
            </div>
            <el-progress
              :percentage="server.cpu"
              :color="getProgressColor(server.cpu)"
              :stroke-width="10"
            />
          </div>

          <!-- Memory -->
          <div class="metric">
            <div class="metric-label">
              <span>Memory</span>
              <span>{{ server.memory }}%</span>
            </div>
            <el-progress
              :percentage="server.memory"
              :color="getProgressColor(server.memory)"
              :stroke-width="10"
            />
          </div>

          <!-- Disk -->
          <div class="metric">
            <div class="metric-label">
              <span>Disk</span>
              <span>{{ server.disk }}%</span>
            </div>
            <el-progress
              :percentage="server.disk"
              :color="getProgressColor(server.disk)"
              :stroke-width="10"
            />
          </div>

          <!-- Network -->
          <div class="metric">
            <div class="metric-label">
              <span>Network I/O</span>
              <span>{{ server.network }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- Grafana 嵌入区域 -->
    <el-card style="margin-top: 20px">
      <template #header>
        <div>📊 Grafana Dashboard</div>
      </template>
      <el-input
        v-model="grafanaUrl"
        placeholder="Grafana dashboard URL"
        style="margin-bottom: 10px"
      >
        <template #prepend>URL</template>
        <template #append>
          <el-button @click="loadGrafana">Load</el-button>
        </template>
      </el-input>

      <iframe
        v-if="iframeUrl"
        :src="iframeUrl"
        width="100%"
        height="400"
        frameborder="0"
      ></iframe>

      <el-empty v-else description="Enter Grafana URL to view dashboard" />
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
// ===== 模拟服务器数据 =====
const servers = ref([
  {
    name: 'prod-api-01',
    status: 'healthy',
    cpu: 45,
    memory: 62,
    disk: 38,
    network: '↓ 125 MB/s  ↑ 89 MB/s'
  },
  {
    name: 'prod-api-02',
    status: 'healthy',
    cpu: 52,
    memory: 71,
    disk: 45,
    network: '↓ 210 MB/s  ↑ 156 MB/s'
  },
  {
    name: 'prod-db-01',
    status: 'warning',
    cpu: 78,
    memory: 85,
    disk: 67,
    network: '↓ 45 MB/s  ↑ 32 MB/s'
  },
  {
    name: 'prod-cache-01',
    status: 'healthy',
    cpu: 23,
    memory: 45,
    disk: 12,
    network: '↓ 890 MB/s  ↑ 756 MB/s'
  },
  {
    name: 'prod-worker-01',
    status: 'healthy',
    cpu: 34,
    memory: 51,
    disk: 28,
    network: '↓ 12 MB/s  ↑ 8 MB/s'
  },
  {
    name: 'prod-worker-02',
    status: 'unhealthy',
    cpu: 95,
    memory: 92,
    disk: 73,
    network: '↓ 2 MB/s  ↑ 1 MB/s'
  }
])
// Grafana
const grafanaUrl = ref('http://localhost:3000/d-solo/os-overview/server-metrics?panelId=2')
const iframeUrl = ref('')
// ===== 方法 =====
function getProgressColor(value) {
  if (value >= 90) return '#f56c6c'  // 红色
  if (value >= 70) return '#e6a23c'  // 橙色
  return '#67c23a'  // 绿色
}
function refreshData() {
  // 模拟刷新数据
  servers.value.forEach(server => {
    server.cpu = Math.floor(Math.random() * 60) + 20
    server.memory = Math.floor(Math.random() * 40) + 40
    server.disk = Math.floor(Math.random() * 30) + 20
  })
  ElMessage.success('Data refreshed')
}
function loadGrafana() {
  if (grafanaUrl.value) {
    iframeUrl.value = grafanaUrl.value
    ElMessage.success('Grafana dashboard loaded')
  }
}
</script>
<style scoped>
.server-card {
  margin-bottom: 20px;
}
.server-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.server-name {
  font-weight: bold;
}
.metric {
  margin-bottom: 15px;
}
.metric-label {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 14px;
  color: #606266;
}
</style>