import request from '@/utils/request';

// ===== 认证 =====
export const login = data => request.post('/auth/login', data);
export const register = data => request.post('/auth/register', data);
export const getInfo = () => request.get('/auth/info');
export const updatePhone = data => request.put('/auth/phone', data);
export const updatePassword = data => request.put('/auth/password', data);

// ===== 分类 =====
export const listCategories = () => request.get('/category/list');
export const addCategory = data => request.post('/admin/category', data);
export const updateCategory = (id, data) => request.put(`/admin/category/${id}`, data);
export const deleteCategory = id => request.delete(`/admin/category/${id}`);

// ===== 活动 =====
export const listActivities = params => request.get('/activity/list', { params });
export const getActivity = id => request.get(`/activity/${id}`);
export const adminListActivities = params => request.get('/admin/activity/list', { params });
export const addActivity = data => request.post('/admin/activity', data);
export const updateActivity = (id, data) => request.put(`/admin/activity/${id}`, data);
export const updateActivityStatus = (id, status) => request.put(`/admin/activity/${id}/status`, null, { params: { status } });
export const deleteActivity = id => request.delete(`/admin/activity/${id}`);

// ===== 报名 =====
export const signup = activityId => request.post('/signup', null, { params: { activityId } });
export const cancelSignup = activityId => request.delete(`/signup/${activityId}`);
export const mySignups = params => request.get('/signup/my', { params });
export const adminListSignups = params => request.get('/admin/signup/list', { params });
export const auditSignup = (id, data) => request.put(`/admin/signup/${id}/audit`, data);

// ===== 后台用户管理 =====
export const adminListUsers = params => request.get('/admin/user/list', { params });
export const adminAddUser = data => request.post('/admin/user', data);
export const adminUpdateUserStatus = (id, status) => request.put(`/admin/user/${id}/status`, null, { params: { status } });
export const adminResetPassword = id => request.put(`/admin/user/${id}/reset-password`);

// ===== 数据概览 =====
export const getStats = () => request.get('/admin/stats');
