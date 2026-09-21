<template>
  <div>
    <el-card shadow="never">
      <div slot="header">个人中心</div>
      <el-row :gutter="24">
        <el-col :xs="24" :md="12">
          <h4 class="section-title">基础信息</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="账号">{{ user.username }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ user.realName }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ user.phone || '未填写' }}</el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ user.createTime }}</el-descriptions-item>
          </el-descriptions>
          <el-button type="primary" icon="el-icon-edit" class="section-btn" @click="phoneDialog = true">
            修改手机号
          </el-button>
        </el-col>
        <el-col :xs="24" :md="12">
          <h4 class="section-title">账号安全</h4>
          <el-button type="warning" icon="el-icon-key" @click="pwdDialog = true">修改登录密码</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 修改手机号 -->
    <el-dialog title="修改手机号" :visible.sync="phoneDialog" width="420px">
      <el-form :model="phoneForm" :rules="phoneRules" ref="phoneFormRef" label-width="80px">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="phoneForm.phone" placeholder="请输入新手机号" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="phoneDialog = false">取消</el-button>
        <el-button type="primary" :loading="savingPhone" @click="submitPhone">保存</el-button>
      </div>
    </el-dialog>

    <!-- 修改密码 -->
    <el-dialog title="修改登录密码" :visible.sync="pwdDialog" width="420px">
      <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="80px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" placeholder="6~20位" show-password />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmNew">
          <el-input v-model="pwdForm.confirmNew" type="password" show-password />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="pwdDialog = false">取消</el-button>
        <el-button type="primary" :loading="savingPwd" @click="submitPassword">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getInfo, updatePhone, updatePassword } from '@/api';

export default {
  name: 'Profile',
  data() {
    const validateConfirm = (rule, value, callback) => {
      if (value !== this.pwdForm.newPassword) {
        callback(new Error('两次输入的新密码不一致'));
      } else {
        callback();
      }
    };
    return {
      user: {},
      phoneDialog: false,
      pwdDialog: false,
      savingPhone: false,
      savingPwd: false,
      phoneForm: { phone: '' },
      phoneRules: {
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ]
      },
      pwdForm: { oldPassword: '', newPassword: '', confirmNew: '' },
      pwdRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度需在6~20位之间', trigger: 'blur' }
        ],
        confirmNew: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validateConfirm, trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.loadUser();
  },
  methods: {
    loadUser() {
      getInfo().then(data => {
        this.user = data;
        this.phoneForm.phone = data.phone || '';
      });
    },
    submitPhone() {
      this.$refs.phoneFormRef.validate(valid => {
        if (!valid) return;
        this.savingPhone = true;
        updatePhone(this.phoneForm)
          .then(() => {
            this.$message.success('手机号修改成功');
            this.phoneDialog = false;
            this.loadUser();
          })
          .finally(() => {
            this.savingPhone = false;
          });
      });
    },
    submitPassword() {
      this.$refs.pwdFormRef.validate(valid => {
        if (!valid) return;
        this.savingPwd = true;
        updatePassword({ oldPassword: this.pwdForm.oldPassword, newPassword: this.pwdForm.newPassword })
          .then(() => {
            this.$message.success('密码修改成功，请重新登录');
            localStorage.removeItem('token');
            localStorage.removeItem('role');
            this.$router.push('/login');
          })
          .finally(() => {
            this.savingPwd = false;
          });
      });
    }
  }
};
</script>

<style scoped>
.section-title {
  color: #303133;
  margin-bottom: 12px;
}
.section-btn {
  margin-top: 16px;
}
</style>
