<template>
  <div>
    <el-card shadow="never">
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="按姓名/账号搜索" clearable style="width: 220px" @keyup.enter.native="handleSearch" @clear="handleSearch" />
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <div class="spacer"></div>
        <el-button type="primary" icon="el-icon-plus" @click="openAdd">新增学生</el-button>
      </div>

      <el-table v-loading="loading" :data="users" stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="username" label="账号" width="140" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="phone" label="手机号" width="140">
          <template slot-scope="{ row }">{{ row.phone || '-' }}</template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="170" />
        <el-table-column label="状态" width="100">
          <template slot-scope="{ row }">
            <el-switch
              :value="row.status === 1"
              :disabled="row.id === currentUserId"
              @change="val => handleStatusChange(row, val)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="150">
          <template slot-scope="{ row }">
            <el-button type="text" :disabled="row.id === currentUserId" @click="handleReset(row)">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && users.length === 0" description="暂无用户" />

      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="query.size"
          :current-page.sync="query.page"
          @current-change="loadUsers"
        />
      </div>
    </el-card>

    <!-- 新增学生 -->
    <el-dialog title="新增学生账号" :visible.sync="addDialog" width="460px">
      <el-form :model="addForm" :rules="addRules" ref="addFormRef" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="addForm.username" placeholder="3~20位字母/数字/下划线" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="addForm.realName" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addForm.password" placeholder="留空则默认 123456" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="addForm.phone" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addDialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitAdd">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { adminListUsers, adminAddUser, adminUpdateUserStatus, adminResetPassword } from '@/api';

export default {
  name: 'UserManage',
  data() {
    return {
      loading: false,
      saving: false,
      users: [],
      total: 0,
      query: { keyword: '', page: 1, size: 10 },
      addDialog: false,
      addForm: { username: '', realName: '', password: '', phone: '' },
      addRules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          { pattern: /^[a-zA-Z0-9_]{3,20}$/, message: '3~20位字母/数字/下划线', trigger: 'blur' }
        ],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ pattern: /^$|^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
      }
    };
  },
  computed: {
    currentUserId() {
      return localStorage.getItem('userId') || '';
    }
  },
  created() {
    this.loadUsers();
  },
  methods: {
    loadUsers() {
      this.loading = true;
      adminListUsers(this.query)
        .then(data => {
          this.users = data.records || [];
          this.total = Number(data.total || 0);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleSearch() {
      this.query.page = 1;
      this.loadUsers();
    },
    openAdd() {
      this.addForm = { username: '', realName: '', password: '', phone: '' };
      this.addDialog = true;
    },
    submitAdd() {
      this.$refs.addFormRef.validate(valid => {
        if (!valid) return;
        this.saving = true;
        adminAddUser(this.addForm)
          .then(() => {
            this.$message.success('新增成功，默认密码为 123456');
            this.addDialog = false;
            this.loadUsers();
          })
          .finally(() => {
            this.saving = false;
          });
      });
    },
    handleStatusChange(row, val) {
      adminUpdateUserStatus(row.id, val ? 1 : 0).then(() => {
        this.$message.success(val ? '已启用' : '已禁用');
        row.status = val ? 1 : 0;
      });
    },
    handleReset(row) {
      this.$confirm(`确定将「${row.realName}」的密码重置为 123456 吗？`, '重置密码', { type: 'warning' })
        .then(() => {
          adminResetPassword(row.id).then(() => {
            this.$message.success('密码已重置为 123456');
          });
        })
        .catch(() => {});
    }
  }
};
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.spacer {
  flex: 1;
}
.pagination-wrap {
  text-align: center;
  margin-top: 16px;
}
</style>
