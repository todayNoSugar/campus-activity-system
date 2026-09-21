<template>
  <el-container class="admin-layout">
    <el-aside width="210px" class="admin-aside">
      <div class="aside-logo">校园活动报名管理</div>
      <el-menu :default-active="$route.path" router background-color="#001529" text-color="#a6adb4" active-text-color="#ffffff">
        <el-menu-item index="/admin/dashboard">
          <i class="el-icon-data-analysis"></i>
          <span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <i class="el-icon-user"></i>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <i class="el-icon-collection-tag"></i>
          <span>活动分类</span>
        </el-menu-item>
        <el-menu-item index="/admin/activities">
          <i class="el-icon-tickets"></i>
          <span>活动管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/signups">
          <i class="el-icon-document-checked"></i>
          <span>报名审核</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="admin-header">
        <span class="header-title">管理后台</span>
        <el-dropdown @command="handleCommand">
          <span class="admin-user">
            管理员：{{ realName }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-header>
      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'AdminLayout',
  computed: {
    realName() {
      return localStorage.getItem('realName') || '管理员';
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗？', '提示', { type: 'warning' })
          .then(() => {
            localStorage.removeItem('token');
            localStorage.removeItem('role');
            localStorage.removeItem('realName');
            this.$router.push('/login');
          })
          .catch(() => {});
      }
    }
  }
};
</script>

<style scoped>
.admin-layout {
  height: 100%;
}
.admin-aside {
  background: #001529;
}
.aside-logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  background: #002140;
}
.admin-aside .el-menu {
  border-right: none;
}
.admin-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 10;
}
.header-title {
  font-size: 16px;
  color: #303133;
}
.admin-user {
  cursor: pointer;
  color: #303133;
}
.admin-main {
  background: #f0f2f5;
}
</style>
