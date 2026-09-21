<template>
  <div class="login-wrap">
    <div class="login-card">
      <h2 class="login-title">校园活动报名管理系统</h2>
      <el-form :model="form" :rules="rules" ref="formRef" @keyup.enter.native="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入账号" prefix-icon="el-icon-user" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="el-icon-lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">登 录</el-button>
        </el-form-item>
        <div class="login-footer">
          还没有账号？
          <router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login } from '@/api';

export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    };
  },
  methods: {
    handleLogin() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return;
        this.loading = true;
        login(this.form)
          .then(data => {
            localStorage.setItem('token', data.token);
            localStorage.setItem('role', data.user.role);
            localStorage.setItem('realName', data.user.realName);
            localStorage.setItem('userId', data.user.id);
            this.$message.success('登录成功');
            const target = Number(data.user.role) === 1 ? '/admin/dashboard' : '/activity';
            this.$router.push(target);
          })
          .finally(() => {
            this.loading = false;
          });
      });
    }
  }
};
</script>

<style scoped>
.login-wrap {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #2b5876 0%, #4e4376 100%);
}
.login-card {
  width: 400px;
  padding: 40px 36px 24px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}
.login-title {
  text-align: center;
  margin-bottom: 28px;
  color: #303133;
  font-size: 22px;
}
.login-btn {
  width: 100%;
}
.login-footer {
  text-align: center;
  color: #909399;
  font-size: 13px;
}
</style>
