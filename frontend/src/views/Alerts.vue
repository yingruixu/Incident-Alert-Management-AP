<template>
  <div class="alerts">
    <!-- 操作栏 -->
    <el-card class="toolbar">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-input
            v-model="search"
            placeholder="Search alert rules..."
            clearable
            style="width: 300px"
          >
            <template #prefix>🔍</template>
          </el-input>

          <el-select v-model="filterEnabled" placeholder="Status" clearable style="margin-left: 10px">
            <el-option label="Enabled" :value="true" />
            <el-option label="Disabled" :value="false" />
          </el-select>
        </el-col>

        <el-col :span="8" style="text-align: right">
          <el-button type="primary" @click="dialogVisible = true">
            ➕ New Alert Rule
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 告警规则列表 -->
    <el-card style="margin-top: 20px">
      <el-table :data="filteredAlerts" style="width: 100%">
        <el-table-column prop="name" label="Rule Name" min-width="180">
          <template #default="{ row }">
            <el-link type="primary">{{ row.name }}</el-link>
          </template>
        </el-table-column>

        <el-table-column prop="metric" label="Metric" width="150" />

        <el-table-column prop="condition" label="Condition" width="150">
          <template #default="{ row }">
            <code>{{ row.threshold }} {{ row.operator }}</code>
          </template>
        </el-table-column>

        <el-table-column prop="severity" label="Severity" width="120">
          <template #default="{ row }">
            <el-tag :type="getSeverityType(row.severity)">
              {{ row.severity.toUpperCase() }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="enabled" label="Status" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.enabled"
              @change="toggleEnabled(row)"
            />
          </template>
        </el-table-column>

        <el-table-column prop="last_triggered" label="Last Triggered" width="180">
          <template #default="{ row }">
            {{ row.last_triggered ? formatDate(row.last_triggered) : 'Never' }}
          </template>
        </el-table-column>

        <el-table-column label="Actions" width="120">
          <template #default="{ row }">
            <el-button size="small" @click="editAlert(row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteAlert(row)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? 'Edit Alert Rule' : 'New Alert Rule'"
      width="600px"
    >
      <el-form :model="form" label-width="140px">
        <el-form-item label="Rule Name">
          <el-input v-model="form.name" placeholder="e.g., High CPU Alert" />
        </el-form-item>

        <el-form-item label="Metric">
          <el-select v-model="form.metric" style="width: 100%" placeholder="Select metric">
            <el-option label="CPU Usage" value="cpu_usage" />
            <el-option label="Memory Usage" value="memory_usage" />
            <el-option label="Disk Usage" value="disk_usage" />
            <el-option label="Request Latency" value="latency" />
            <el-option label="Error Rate" value="error_rate" />
          </el-select>
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="Operator">
              <el-select v-model="form.operator" style="width: 100%">
                <el-option label=">" value=">" />
                <el-option label=">=" value=">=" />
                <el-option label="<" value="<" />
                <el-option label="<=" value="<=" />
                <el-option label="==" value="==" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="16">
            <el-form-item label="Threshold">
              <el-input v-model="form.threshold" type="number" placeholder="e.g., 90">
                <template #append>%</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="Severity">
          <el-select v-model="form.severity" style="width: 100%">
            <el-option label="Critical" value="critical" />
            <el-option label="High" value="high" />
            <el-option label="Medium" value="medium" />
            <el-option label="Low" value="low" />
          </el-select>
        </el-form-item>

        <el-form-item label="Notification Channel">
          <el-select v-model="form.channel" style="width: 100%">
            <el-option label="Email" value="email" />
            <el-option label="Slack" value="slack" />
            <el-option label="PagerDuty" value="pagerduty" />
            <el-option label="Webhook" value="webhook" />
          </el-select>
        </el-form-item>

        <el-form-item label="Enabled">
          <el-switch v-model="form.enabled" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveAlert">
          {{ isEdit ? 'Update' : 'Create' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// ===== 响应式数据 =====
const search = ref('')
const filterEnabled = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)

// 模拟告警规则数据
const alerts = ref([
  {
    id: 1,
    name: 'High CPU Usage',
    metric: 'cpu_usage',
    operator: '>',
    threshold: 90,
    severity: 'critical',
    enabled: true,
    channel: 'slack',
    last_triggered: '2026-03-15T08:30:00Z'
  },
  {
    id: 2,
    name: 'Memory Leak Detection',
    metric: 'memory_usage',
    operator: '>',
    threshold: 85,
    severity: 'high',
    enabled: true,
    channel: 'email',
    last_triggered: '2026-03-14T22:15:00Z'
  },
  {
    id: 3,
    name: 'Disk Space Low',
    metric: 'disk_usage',
    operator: '>',
    threshold: 95,
    severity: 'critical',
    enabled: true,
    channel: 'slack',
    last_triggered: null
  },
  {
    id: 4,
    name: 'High Latency',
    metric: 'latency',
    operator: '>',
    threshold: 500,
    severity: 'medium',
    enabled: false,
    channel: 'email',
    last_triggered: '2026-03-10T14:00:00Z'
  }
])

// 表单数据
const form = ref({
  id: null,
  name: '',
  metric: 'cpu_usage',
  operator: '>',
  threshold: 90,
  severity: 'high',
  channel: 'email',
  enabled: true
})

// ===== 计算属性 =====
const filteredAlerts = computed(() => {
  return alerts.value.filter(item => {
    const matchSearch = !search.value ||
      item.name.toLowerCase().includes(search.value.toLowerCase()) ||
      item.metric.toLowerCase().includes(search.value.toLowerCase())

    const matchEnabled = filterEnabled.value === '' || item.enabled === filterEnabled.value

    return matchSearch && matchEnabled
  })
})

// ===== 方法 =====

function getSeverityType(severity) {
  const map = {
    critical: 'danger',
    high: 'warning',
    medium: 'info',
    low: 'success'
  }
  return map[severity] || 'info'
}

function formatDate(dateStr) {
  if (!dateStr) return 'Never'
  return new Date(dateStr).toLocaleString()
}

function toggleEnabled(row) {
  ElMessage.success(`Alert ${row.enabled ? 'enabled' : 'disabled'}`)
}

function editAlert(row) {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

function saveAlert() {
  if (!form.value.name || !form.value.threshold) {
    ElMessage.warning('Please fill required fields')
    return
  }

  if (isEdit.value) {
    const index = alerts.value.findIndex(a => a.id === form.value.id)
    if (index !== -1) {
      alerts.value[index] = { ...form.value }
    }
    ElMessage.success('Alert rule updated')
  } else {
    const newId = Math.max(...alerts.value.map(a => a.id)) + 1
    alerts.value.unshift({
      ...form.value,
      id: newId,
      last_triggered: null
    })
    ElMessage.success('Alert rule created')
  }

  dialogVisible.value = false
}

function deleteAlert(row) {
  ElMessageBox.confirm(
    `Delete alert rule "${row.name}"?`,
    'Warning',
    { type: 'warning' }
  ).then(() => {
    const index = alerts.value.findIndex(a => a.id === row.id)
    if (index !== -1) {
      alerts.value.splice(index, 1)
    }
    ElMessage.success('Deleted')
  }).catch(() => {})
}
</script>

<style scoped>
.toolbar {
  margin-bottom: 20px;
}

code {
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 13px;
}
</style>