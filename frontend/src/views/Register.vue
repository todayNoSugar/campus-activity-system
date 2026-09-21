<template>
  <div class="register-wrap">
    <div class="register-card">
      <h2 class="register-title">学生注册</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="72px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" placeholder="3~20位字母/数字/下划线" clearable />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="6~20位" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="再次输入密码" show-password />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="选填" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="register-btn" :loading="loading" @click="handleRegister">注 册</el-button>
        </el-form-item>
        <div class="register-footer">
          已有账号？
          <router-link to="/login">去登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { register } from '@/api';

export default {
  name: 'Register',
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.form.password) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      form: { username: '', realName: '', password: '', confirmPassword: '', phone: '' },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]{3,20}$/, message: '3~20位字母/数字/下划线', trigger: 'blur' }
        ],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度需在6~20位之间', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ],
        phone: [{ pattern: /^$|^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
      }
    };
  },
  methods: {
    handleRegister() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return;
        this.loading = true;
        const { confirmPassword, ...payload } = this.form;
        register(payload)
          .then(() => {
            this.$message.success('注册成功，请登录');
            this.$router.push('/login');
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
.register-wrap {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #2b5876 0%, #4e4376 100%);
}
.register-card {
  width: 440px;
  padding: 32px 36px 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}
.register-title {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
  font-size: 20px;
}
.register-btn {
  width: 100%;
}
.register-footer {
  text-align: center;
  color: #909399;
  font-size: 13px;
}
</style>
