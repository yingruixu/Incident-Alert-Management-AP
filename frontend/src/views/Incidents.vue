import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { incidentsApi } from '../api'

<template>
  <div class="incidents">
    <!-- 头部操作栏 -->
    <el-card class="toolbar">
      <el-row :gutter="20">
        <el-col :span="18">
          <el-input
            v-model="search"
            placeholder="Search incidents..."
            clearable
            style="width: 300px"
          >
            <template #prefix>🔍</template>
          </el-input>

          <el-select v-model="filterStatus" placeholder="Status" clearable style="margin-left: 10px">
            <el-option label="Open" value="open" />
            <el-option label="In Progress" value="in_progress" />
            <el-option label="Resolved" value="resolved" />
            <el-option label="Closed" value="closed" />
          </el-select>

          <el-select v-model="filterSeverity" placeholder="Severity" clearable style="margin-left: 10px">
            <el-option label="Critical" value="critical" />
            <el-option label="High" value="high" />
            <el-option label="Medium" value="medium" />
            <el-option label="Low" value="low" />
          </el-select>
        </el-col>

        <el-col :span="6" style="text-align: right">
          <el-button type="primary" @click="dialogVisible = true">
            ➕ New Incident
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 事件列表表格 -->
    <el-card style="margin-top: 20px">
      <el-table :data="filteredIncidents" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="title" label="Title" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="viewDetail(row)">
              {{ row.title }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="severity" label="Severity" width="120">
          <template #default="{ row }">
            <el-tag :type="getSeverityType(row.severity)">
              {{ row.severity.toUpperCase() }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ formatStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="service" label="Service" width="150" />

        <el-table-column prop="created_at" label="Created" width="180">
          <template #default="{ row }">
            {{ formatDate(row.created_at) }}
          </template>
        </el-table-column>

        <el-table-column label="Actions" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="editIncident(row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteIncident(row)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? 'Edit Incident' : 'New Incident'"
      width="600px"
    >
      <el-form :model="form" label-width="120px">
        <el-form-item label="Title">
          <el-input v-model="form.title" placeholder="Incident title" />
        </el-form-item>

        <el-form-item label="Description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="What happened?"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Severity">
              <el-select v-model="form.severity" style="width: 100%">
                <el-option label="Critical" value="critical" />
                <el-option label="High" value="high" />
                <el-option label="Medium" value="medium" />
                <el-option label="Low" value="low" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Status">
              <el-select v-model="form.status" style="width: 100%">
                <el-option label="Open" value="open" />
                <el-option label="In Progress" value="in_progress" />
                <el-option label="Resolved" value="resolved" />
                <el-option label="Closed" value="closed" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="Service">
          <el-input v-model="form.service" placeholder="e.g., payment-api" />
        </el-form-item>

        <el-form-item label="Assignee">
          <el-input v-model="form.assignee" placeholder="Who is handling this?" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="saveIncident">
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
const filterStatus = ref('')
const filterSeverity = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)

const incidents = ref([])

// 页面加载时获取数据
onMounted(() => {
  loadIncidents()
})

async function loadIncidents() {
  try {
    const res = await incidentsApi.getAll()
    incidents.value = res
  } catch (error) {
    console.error('Failed to load:', error)
    ElMessage.error('Failed to load incidents')
  }
}

// 表单数据
const form = ref({
  id: null,
  title: '',
  description: '',
  severity: 'medium',
  status: 'open',
  service: '',
  assignee: ''
})

// ===== 计算属性 =====
// 根据搜索和过滤条件筛选事件
const filteredIncidents = computed(() => {
  return incidents.value.filter(item => {
    const matchSearch = !search.value ||
      item.title.toLowerCase().includes(search.value.toLowerCase()) ||
      item.service.toLowerCase().includes(search.value.toLowerCase())

    const matchStatus = !filterStatus.value || item.status === filterStatus.value
    const matchSeverity = !filterSeverity.value || item.severity === filterSeverity.value

    return matchSearch && matchStatus && matchSeverity
  })
})

// ===== 方法 =====

// 严重程度标签颜色
function getSeverityType(severity) {
  const map = {
    critical: 'danger',
    high: 'warning',
    medium: 'info',
    low: 'success'
  }
  return map[severity] || 'info'
}

// 状态标签颜色
function getStatusType(status) {
  const map = {
    open: 'danger',
    in_progress: 'warning',
    resolved: 'success',
    closed: 'info'
  }
  return map[status] || 'info'
}

// 格式化状态显示
function formatStatus(status) {
  const map = {
    open: 'Open',
    in_progress: 'In Progress',
    resolved: 'Resolved',
    closed: 'Closed'
  }
  return map[status] || status
}

// 格式化日期
function formatDate(dateStr) {
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 新建事件
function editIncident(row) {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

// 查看详情
function viewDetail(row) {
  ElMessageBox.alert(row.description, row.title)
}

// 保存事件
async function saveIncident() {
  try {
    if (isEdit.value) {
      await incidentsApi.update(form.value.id, form.value)
    } else {
      await incidentsApi.create(form.value)
    }
    await loadIncidents()
    dialogVisible.value = false
    ElMessage.success('Saved!')
  } catch (error) {
    ElMessage.error('Failed to save')
  }
}

// 删除事件
async function deleteIncident(row) {
  try {
    await ElMessageBox.confirm('Delete this incident?', 'Warning', { type: 'warning' })
    await incidentsApi.delete(row.id)
    await loadIncidents()
    ElMessage.success('Deleted')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Failed to delete')
    }
  }
}
</script>

<style scoped>
.toolbar {
  margin-bottom: 20px;
}
</style>