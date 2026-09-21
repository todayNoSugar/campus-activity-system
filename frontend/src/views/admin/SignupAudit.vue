<template>
  <div>
    <el-card shadow="never">
      <div class="toolbar">
        <el-select v-model="query.activityId" placeholder="按活动筛选" clearable filterable style="width: 200px" @change="handleSearch">
          <el-option v-for="a in activities" :key="a.id" :label="a.title" :value="a.id" />
        </el-select>
        <el-select v-model="query.status" placeholder="审核状态" clearable style="width: 130px; margin-left: 12px" @change="handleSearch">
          <el-option label="待审核" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已驳回" :value="2" />
        </el-select>
        <el-input v-model="query.keyword" placeholder="按活动/学生姓名/账号搜索" clearable style="width: 220px; margin-left: 12px" @keyup.enter.native="handleSearch" @clear="handleSearch" />
        <el-button type="primary" icon="el-icon-search" style="margin-left: 12px" @click="handleSearch">搜索</el-button>
      </div>

      <el-table v-loading="loading" :data="signups" stripe>
        <el-table-column prop="activityTitle" label="活动名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="realName" label="学生姓名" width="110" />
        <el-table-column prop="username" label="账号" width="110" />
        <el-table-column prop="phone" label="手机号" width="130">
          <template slot-scope="{ row }">{{ row.phone || '-' }}</template>
        </el-table-column>
        <el-table-column prop="signupTime" label="报名时间" width="160" />
        <el-table-column label="审核状态" width="100">
          <template slot-scope="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="{ row }">
            <template v-if="row.status === 0">
              <el-button type="text" style="color: #67c23a" @click="handleAudit(row, 1)">通过</el-button>
              <el-button type="text" style="color: #f56c6c" @click="handleAudit(row, 2)">驳回</el-button>
            </template>
            <el-button v-else type="text" @click="viewRemark(row)">查看备注</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && signups.length === 0" description="暂无报名记录" />

      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="query.size"
          :current-page.sync="query.page"
          @current-change="loadSignups"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { adminListSignups, adminListActivities, auditSignup } from '@/api';

export default {
  name: 'SignupAudit',
  data() {
    return {
      loading: false,
      signups: [],
      activities: [],
      total: 0,
      query: { activityId: null, status: null, keyword: '', page: 1, size: 10 }
    };
  },
  created() {
    this.loadActivities();
    this.loadSignups();
  },
  methods: {
    loadActivities() {
      adminListActivities({ page: 1, size: 200 }).then(data => {
        this.activities = data.records || [];
      });
    },
    loadSignups() {
      this.loading = true;
      adminListSignups(this.query)
        .then(data => {
          this.signups = data.records || [];
          this.total = Number(data.total || 0);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleSearch() {
      this.query.page = 1;
      this.loadSignups();
    },
    statusText(status) {
      return { 0: '待审核', 1: '已通过', 2: '已驳回' }[status] || '未知';
    },
    statusType(status) {
      return { 0: 'warning', 1: 'success', 2: 'danger' }[status] || 'info';
    },
    handleAudit(row, status) {
      const action = status === 1 ? '通过' : '驳回';
      this.$prompt(status === 2 ? '请输入驳回原因（可选）' : '备注（可选）', `${action}报名`, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: status === 2 ? '如：名额已满、不符合报名条件' : '选填'
      })
        .then(({ value }) => {
          auditSignup(row.id, { status, remark: value || null }).then(() => {
            this.$message.success(`${action}成功`);
            this.loadSignups();
          });
        })
        .catch(() => {});
    },
    viewRemark(row) {
      this.$alert(row.remark || '无备注', '审核备注', { confirmButtonText: '关闭' });
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
.pagination-wrap {
  text-align: center;
  margin-top: 16px;
}
</style>
