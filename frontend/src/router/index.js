import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

const router = new Router({
  mode: 'history',
  routes: [
    { path: '/login', component: () => import('@/views/Login.vue'), meta: { public: true } },
    { path: '/register', component: () => import('@/views/Register.vue'), meta: { public: true } },
    // 学生端（公共布局）
    {
      path: '/',
      component: () => import('@/views/student/StudentLayout.vue'),
      children: [
        { path: '', redirect: '/activity' },
        { path: 'activity', component: () => import('@/views/student/ActivityList.vue') },
        { path: 'activity/:id', component: () => import('@/views/student/ActivityDetail.vue') },
        { path: 'my-signups', component: () => import('@/views/student/MySignups.vue') },
        { path: 'profile', component: () => import('@/views/student/Profile.vue') }
      ]
    },
    // 管理端
    {
      path: '/admin',
      component: () => import('@/views/admin/AdminLayout.vue'),
      meta: { admin: true },
      children: [
        { path: '', redirect: '/admin/dashboard' },
        { path: 'dashboard', component: () => import('@/views/admin/Dashboard.vue') },
        { path: 'users', component: () => import('@/views/admin/UserManage.vue') },
        { path: 'categories', component: () => import('@/views/admin/CategoryManage.vue') },
        { path: 'activities', component: () => import('@/views/admin/ActivityManage.vue') },
        { path: 'signups', component: () => import('@/views/admin/SignupAudit.vue') }
      ]
    },
    { path: '*', redirect: '/activity' }
  ]
});

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const role = Number(localStorage.getItem('role'));
  if (to.meta.public) {
    if (token && to.path === '/login') {
      next(role === 1 ? '/admin/dashboard' : '/activity');
      return;
    }
    next();
    return;
  }
  if (!token) {
    next('/login');
    return;
  }
  if (to.meta.admin && role !== 1) {
    next('/activity');
    return;
  }
  next();
});

export default router;
