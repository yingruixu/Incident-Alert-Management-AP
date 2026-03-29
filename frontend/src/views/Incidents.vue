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
            <el-option label="Open" value="OPEN" />
            <el-option label="Closed" value="CLOSED" />
          </el-select>

          <el-select v-model="filterSeverity" placeholder="Severity" clearable style="margin-left: 10px">
            <el-option label="P0 - Critical" value="P0" />
            <el-option label="P1 - High" value="P1" />
            <el-option label="P2 - Medium" value="P2" />
            <el-option label="P3 - Low" value="P3" />
            <el-option label="P4 - Lowest" value="P4" />
          </el-select>
        </el-col>

        <el-col :span="6" style="text-align: right">
          <el-button type="primary" @click="openNewDialog">
            ➕ New Incident
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 加载状态 -->
    <div v-if="loading" style="text-align:center; padding:40px">
      <el-icon class="is-loading" style="font-size: 32px"><Loading /></el-icon>
    </div>

    <!-- 事件列表表格 -->
    <el-card v-else style="margin-top: 20px">
      <el-table :data="filteredIncidents" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="title" label="Title" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="viewDetail(row)">
              {{ row.title }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="severity" label="Severity" width="100">
          <template #default="{ row }">
            <el-tag :type="getSeverityType(row.severity)">
              {{ row.severity }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createdAt" label="Created" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>

        <el-table-column label="Actions" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="openEditDialog(row)">Edit</el-button>
            <el-button size="small" type="warning" @click="toggleStatus(row)" v-if="row.status === 'OPEN'">
              Resolve
            </el-button>
            <el-button size="small" type="danger" @click="deleteIncident(row)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空状态 -->
      <el-empty v-if="!filteredIncidents.length" description="No incidents found" />
    </el-card>

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? 'Edit Incident' : 'New Incident'"
      width="500px"
    >
      <el-form :model="form" label-width="120px" ref="formRef">
        <el-form-item label="Title" required>
          <el-input v-model="form.title" placeholder="Incident title" />
        </el-form-item>

        <el-form-item label="Severity" required>
          <el-select v-model="form.severity" placeholder="Select severity" style="width: 100%">
            <el-option label="P0 - Critical" value="P0" />
            <el-option label="P1 - High" value="P1" />
            <el-option label="P2 - Medium" value="P2" />
            <el-option label="P3 - Low" value="P3" />
            <el-option label="P4 - Lowest" value="P4" />
          </el-select>
        </el-form-item>

        <el-form-item label="Status" v-if="isEdit">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="Open" value="OPEN" />
            <el-option label="Closed" value="CLOSED" />
          </el-select>
        </el-form-item>

        <el-form-item label="Description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="What happened? (optional)"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" :loading="saving" @click="saveIncident">
          {{ isEdit ? 'Update' : 'Create' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Loading } from '@element-plus/icons-vue'
import { incidentsApi } from '../api'

// ===== 响应式数据 =====
const search = ref('')
const filterStatus = ref('')
const filterSeverity = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const saving = ref(false)
const loading = ref(false)

const incidents = ref([])

const form = ref({
  id: null,
  title: '',
  description: '',
  severity: 'P2',
  status: 'OPEN'
})

// ===== 页面加载时获取数据 =====
onMounted(() => {
  loadIncidents()
})

async function loadIncidents() {
  loading.value = true
  try {
    const data = await incidentsApi.getAll()
    incidents.value = data || []
  } catch (error) {
    console.error('Failed to load incidents:', error)
    ElMessage.error('Failed to load incidents')
  } finally {
    loading.value = false
  }
}

// ===== 计算属性 =====
const filteredIncidents = computed(() => {
  return incidents.value.filter(item => {
    const matchSearch = !search.value ||
      item.title?.toLowerCase().includes(search.value.toLowerCase())

    const matchStatus = !filterStatus.value || item.status === filterStatus.value
    const matchSeverity = !filterSeverity.value || item.severity === filterSeverity.value

    return matchSearch && matchStatus && matchSeverity
  })
})

// ===== 方法 =====

function getSeverityType(severity) {
  const map = { P0: 'danger', P1: 'warning', P2: 'info', P3: 'success', P4: '' }
  return map[severity] || 'info'
}

function getStatusType(status) {
  const map = { OPEN: 'danger', CLOSED: 'success' }
  return map[status] || 'info'
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('en-US', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  })
}

function openNewDialog() {
  isEdit.value = false
  form.value = { id: null, title: '', description: '', severity: 'P2', status: 'OPEN' }
  dialogVisible.value = true
}

function openEditDialog(row) {
  isEdit.value = true
  form.value = {
    id: row.id,
    title: row.title || '',
    description: row.description || '',
    severity: row.severity || 'P2',
    status: row.status || 'OPEN'
  }
  dialogVisible.value = true
}

function viewDetail(row) {
  const msg = row.description
    ? `<b>${row.title}</b><br/><br/>${row.description}<br/><br/>Severity: ${row.severity} | Status: ${row.status}`
    : `<b>${row.title}</b><br/><br/>Severity: ${row.severity} | Status: ${row.status}<br/>Created: ${formatDate(row.createdAt)}`
  ElMessageBox.alert(msg, 'Incident Detail', {
    dangerouslyUseHTMLString: true,
    confirmButtonText: 'OK'
  })
}

async function saveIncident() {
  if (!form.value.title || !form.value.severity) {
    ElMessage.warning('Title and Severity are required')
    return
  }

  saving.value = true
  try {
    if (isEdit.value) {
      await incidentsApi.update(form.value.id, {
        title: form.value.title,
        description: form.value.description,
        severity: form.value.severity,
        status: form.value.status
      })
    } else {
      await incidentsApi.create({
        title: form.value.title,
        description: form.value.description,
        severity: form.value.severity
      })
    }
    dialogVisible.value = false
    await loadIncidents()
    ElMessage.success(isEdit.value ? 'Updated!' : 'Created!')
  } catch (error) {
    console.error('Save failed:', error)
    ElMessage.error(error?.response?.data || 'Failed to save')
  } finally {
    saving.value = false
  }
}

async function toggleStatus(row) {
  try {
    await ElMessageBox.confirm(
      `Mark incident "${row.title}" as CLOSED?`,
      'Resolve Incident',
      { type: 'warning' }
    )
    // PATCH /incidents/{id}/status?status=CLOSED
    await incidentsApi.update(row.id, { status: 'CLOSED' })
    await loadIncidents()
    ElMessage.success('Incident resolved')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Failed to update status')
    }
  }
}

async function deleteIncident(row) {
  try {
    await ElMessageBox.confirm(
      `Delete incident "${row.title}"?`,
      'Warning',
      { type: 'warning' }
    )
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