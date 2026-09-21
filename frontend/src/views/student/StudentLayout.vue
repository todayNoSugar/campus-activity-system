<template>
  <div class="student-layout">
    <header class="student-header">
      <div class="header-left">
        <span class="logo">🎓 校园活动报名</span>
        <el-menu mode="horizontal" :default-active="activeMenu" router class="header-menu">
          <el-menu-item index="/activity">活动大厅</el-menu-item>
          <el-menu-item index="/my-signups">我的报名</el-menu-item>
          <el-menu-item index="/profile">个人中心</el-menu-item>
        </el-menu>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-name">
            {{ realName }}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </header>
    <main class="student-main">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'StudentLayout',
  computed: {
    activeMenu() {
      return '/' + this.$route.path.split('/')[1];
    },
    realName() {
      return localStorage.getItem('realName') || '同学';
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'profile') {
        this.$router.push('/profile');
      } else if (command === 'logout') {
        this.handleLogout();
      }
    },
    handleLogout() {
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
};
</script>

<style scoped>
.student-layout {
  min-height: 100%;
  display: flex;
  flex-direction: column;
}
.student-header {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-left {
  display: flex;
  align-items: center;
}
.logo {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-right: 32px;
}
.header-menu {
  border-bottom: none;
}
.header-right {
  display: flex;
  align-items: center;
}
.user-name {
  cursor: pointer;
  color: #303133;
}
.student-main {
  flex: 1;
  padding: 20px 24px;
}
</style>
