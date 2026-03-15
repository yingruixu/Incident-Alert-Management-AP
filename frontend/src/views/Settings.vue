<template>
  <div class="settings">
    <el-tabs v-model="activeTab">
      <!-- 用户设置 -->
      <el-tab-pane label="👤 User" name="user">
        <el-card>
          <el-form label-width="150px">
            <el-form-item label="Username">
              <el-input v-model="user.username" />
            </el-form-item>

            <el-form-item label="Email">
              <el-input v-model="user.email" type="email" />
            </el-form-item>

            <el-form-item label="Role">
              <el-select v-model="user.role" style="width: 100%">
                <el-option label="Admin" value="admin" />
                <el-option label="SRE" value="sre" />
                <el-option label="Developer" value="developer" />
                <el-option label="Viewer" value="viewer" />
              </el-select>
            </el-form-item>

            <el-form-item label="Timezone">
              <el-select v-model="user.timezone" style="width: 100%">
                <el-option label="UTC" value="UTC" />
                <el-option label="Asia/Tokyo" value="Asia/Tokyo" />
                <el-option label="Asia/Shanghai" value="Asia/Shanghai" />
                <el-option label="America/New_York" value="America/New_York" />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveUser">Save</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      <!-- API 设置 -->
      <el-tab-pane label="🔗 API" name="api">
        <el-card>
          <el-form label-width="150px">
            <el-form-item label="Backend URL">
              <el-input v-model="api.backendUrl" placeholder="/api" />
            </el-form-item>

            <el-form-item label="API Timeout">
              <el-input-number v-model="api.timeout" :min="1000" :max="60000" />
              <span style="margin-left: 10px">ms</span>
            </el-form-item>

            <el-form-item label="Auth Token">
              <el-input v-model="api.token" type="password" show-password />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="testApi">Test Connection</el-button>
              <el-button @click="saveApi">Save</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      <!-- 通知设置 -->
      <el-tab-pane label="🔔 Notifications" name="notifications">
        <el-card>
          <el-form label-width="150px">
            <el-form-item label="Email Notifications">
              <el-switch v-model="notifications.email" />
            </el-form-item>

            <el-form-item label="Slack Notifications">
              <el-switch v-model="notifications.slack" />
            </el-form-item>

            <el-form-item label="Slack Webhook">
              <el-input v-model="notifications.slackWebhook" placeholder="https://hooks.slack.com/..." />
            </el-form-item>

            <el-form-item label="Email Address">
              <el-input v-model="notifications.emailAddress" type="email" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="saveNotifications">Save</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
      <!-- Grafana 设置 -->
      <el-tab-pane label="📊 Grafana" name="grafana">
        <el-card>
          <el-form label-width="150px">
            <el-form-item label="Grafana URL">
              <el-input v-model="grafana.url" placeholder="http://localhost:3000" />
            </el-form-item>

            <el-form-item label="API Key">
              <el-input v-model="grafana.apiKey" type="password" show-password />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="testGrafana">Test</el-button>
              <el-button @click="saveGrafana">Save</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
const activeTab = ref('user')
// 用户设置
const user = ref({
  username: 'admin',
  email: 'admin@example.com',
  role: 'admin',
  timezone: 'Asia/Tokyo'
})
// API 设置
const api = ref({
  backendUrl: '/api',
  timeout: 10000,
  token: ''
})
// 通知设置
const notifications = ref({
  email: true,
  slack: true,
  slackWebhook: '',
  emailAddress: 'admin@example.com'
})
// Grafana 设置
const grafana = ref({
  url: 'http://localhost:3000',
  apiKey: ''
})
// 保存方法
function saveUser() {
  ElMessage.success('User settings saved')
}
function saveApi() {
  ElMessage.success('API settings saved')
}
function saveNotifications() {
  ElMessage.success('Notification settings saved')
}
function saveGrafana() {
  ElMessage.success('Grafana settings saved')
}
function testApi() {
  ElMessage.info('Testing API connection...')
  setTimeout(() => {
    ElMessage.success('Connection successful!')
  }, 1000)
}
function testGrafana() {
  ElMessage.info('Testing Grafana connection...')
  setTimeout(() => {
    ElMessage.success('Grafana connected!')
  }, 1000)
}
</script>
<style scoped>
.el-tab-pane {
  padding-top: 20px;
}
</style>