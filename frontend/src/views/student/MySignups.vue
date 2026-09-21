<template>
  <div>
    <el-card shadow="never">
      <div slot="header">我的报名</div>
      <el-table v-loading="loading" :data="records" stripe>
        <el-table-column prop="activityTitle" label="活动名称" min-width="160" />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="activityLocation" label="地点" min-width="140" />
        <el-table-column prop="activityStartTime" label="活动开始" width="160" />
        <el-table-column label="审核状态" width="110">
          <template slot-scope="{ row }">
            <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
        <el-table-column prop="signupTime" label="报名时间" width="160" />
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" @click="$router.push('/activity/' + row.activityId)">查看</el-button>
            <el-button type="text" style="color: #f56c6c" :disabled="!canCancel(row)" @click="handleCancel(row)">
              取消报名
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && records.length === 0" description="暂无报名记录" />
      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="query.size"
          :current-page.sync="query.page"
          @current-change="loadRecords"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import { mySignups, cancelSignup } from '@/api';

export default {
  name: 'MySignups',
  data() {
    return {
      loading: false,
      records: [],
      total: 0,
      query: { page: 1, size: 10 }
    };
  },
  created() {
    this.loadRecords();
  },
  methods: {
    loadRecords() {
      this.loading = true;
      mySignups(this.query)
        .then(data => {
          this.records = data.records || [];
          this.total = Number(data.total || 0);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    statusText(status) {
      return { 0: '待审核', 1: '已通过', 2: '已驳回' }[status] || '未知';
    },
    statusType(status) {
      return { 0: 'warning', 1: 'success', 2: 'danger' }[status] || 'info';
    },
    canCancel(row) {
      // 活动未开始前可取消
      return new Date(row.activityStartTime).getTime() > Date.now();
    },
    handleCancel(row) {
      this.$confirm(`确定取消「${row.activityTitle}」的报名吗？`, '取消确认', { type: 'warning' })
        .then(() => {
          cancelSignup(row.activityId).then(() => {
            this.$message.success('已取消报名');
            this.loadRecords();
          });
        })
        .catch(() => {});
    }
  }
};
</script>

<style scoped>
.pagination-wrap {
  text-align: center;
  margin-top: 16px;
}
</style>
