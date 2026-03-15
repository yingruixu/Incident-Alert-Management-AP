<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>🔐 Login</h2>
        </div>
      </template>

      <el-form :model="form" @submit.prevent="handleLogin">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="Username"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="Password"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            style="width: 100%"
            @click="handleLogin"
          >
            {{ loading ? 'Logging in...' : 'Login' }}
          </el-button>
        </el-form-item>
      </el-form>

      <el-alert
        v-if="error"
        :title="error"
        type="error"
        show-icon
      />
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
const router = useRouter()
const form = ref({ username: '', password: '' })
const loading = ref(false)
const error = ref('')
const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    error.value = 'Please enter username and password'
    return
  }
  loading.value = true
  error.value = ''
  try {
    const response = await axios.post('/auth/login', {
      username: form.value.username,
      password: form.value.password
    })
    localStorage.setItem('token', response.data)
    localStorage.setItem('username', form.value.username)

    ElMessage.success('Login successful!')
    router.push('/dashboard')

  } catch (err) {
    error.value = 'Login failed: ' + (err.response?.data || err.message)
  } finally {
    loading.value = false
  }
}
</script>
<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card { width: 400px; }
.card-header h2 { text-align: center; margin: 0; }
</style>