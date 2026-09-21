<template>
  <div v-loading="loading">
    <el-row :gutter="16">
      <el-col v-for="card in statCards" :key="card.label" :xs="12" :sm="8" :md="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon" :style="{ background: card.color }">
            <i :class="card.icon"></i>
          </div>
          <div class="stat-body">
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="second-row">
      <el-col :xs="24" :md="14">
        <el-card shadow="never">
          <div slot="header">各分类活动数量分布</div>
          <el-empty v-if="!categoryDistribution || categoryDistribution.length === 0" description="暂无数据" />
          <div v-for="item in categoryDistribution" :key="item.name" class="category-bar">
            <span class="category-name">{{ item.name }}</span>
            <div class="bar-track">
              <div class="bar-fill" :style="{ width: barWidth(item.value) }"></div>
            </div>
            <span class="category-count">{{ item.value }} 个</span>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="10">
        <el-card shadow="never">
          <div slot="header">快捷入口</div>
          <div class="quick-links">
            <el-button type="primary" @click="$router.push('/admin/activities')">发布新活动</el-button>
            <el-button type="warning" @click="$router.push('/admin/signups')">审核报名</el-button>
            <el-button type="success" @click="$router.push('/admin/users')">管理用户</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStats } from '@/api';

export default {
  name: 'Dashboard',
  data() {
    return {
      loading: false,
      stats: {
        totalUsers: 0,
        totalActivities: 0,
        totalSignups: 0,
        ongoingActivities: 0,
        pendingSignups: 0,
        categoryDistribution: []
      }
    };
  },
  computed: {
    statCards() {
      return [
        { label: '学生总数', value: this.stats.totalUsers, icon: 'el-icon-user', color: '#409eff' },
        { label: '活动总数', value: this.stats.totalActivities, icon: 'el-icon-tickets', color: '#67c23a' },
        { label: '报名总数', value: this.stats.totalSignups, icon: 'el-icon-document', color: '#e6a23c' },
        { label: '进行中活动', value: this.stats.ongoingActivities, icon: 'el-icon-time', color: '#909399' },
        { label: '待审核报名', value: this.stats.pendingSignups, icon: 'el-icon-bell', color: '#f56c6c' }
      ];
    },
    categoryDistribution() {
      return this.stats.categoryDistribution || [];
    }
  },
  created() {
    this.loadStats();
  },
  methods: {
    loadStats() {
      this.loading = true;
      getStats()
        .then(data => {
          this.stats = data;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    barWidth(value) {
      const max = Math.max(...this.categoryDistribution.map(i => Number(i.value)), 1);
      return Math.round((Number(value) / max) * 100) + '%';
    }
  }
};
</script>

<style scoped>
.stat-card {
  margin-bottom: 16px;
}
.stat-card /deep/ .el-card__body {
  display: flex;
  align-items: center;
}
.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: 8px;
  color: #fff;
  font-size: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}
.stat-value {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}
.stat-label {
  font-size: 13px;
  color: #909399;
}
.second-row {
  margin-top: 4px;
}
.category-bar {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
}
.category-name {
  width: 80px;
  color: #606266;
  font-size: 13px;
}
.bar-track {
  flex: 1;
  height: 18px;
  background: #f0f2f5;
  border-radius: 9px;
  margin: 0 12px;
}
.bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #66b1ff);
  border-radius: 9px;
  transition: width 0.4s;
}
.category-count {
  width: 48px;
  text-align: right;
  color: #606266;
  font-size: 13px;
}
.quick-links {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
</style>
