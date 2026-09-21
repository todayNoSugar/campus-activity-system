<template>
  <div>
    <!-- 搜索区 -->
    <el-card class="search-card" shadow="never">
      <el-form inline>
        <el-form-item label="活动名称">
          <el-input v-model="query.keyword" placeholder="输入活动名称搜索" clearable style="width: 220px" @keyup.enter.native="handleSearch" @clear="handleSearch" />
        </el-form-item>
        <el-form-item label="活动分类">
          <el-select v-model="query.categoryId" placeholder="全部分类" clearable style="width: 160px" @change="handleSearch">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 活动列表 -->
    <div v-loading="loading">
      <el-empty v-if="!loading && activities.length === 0" description="暂无活动" />
      <el-row :gutter="16">
        <el-col v-for="a in activities" :key="a.id" :xs="24" :sm="12" :md="8" :lg="6">
          <el-card class="activity-card" shadow="hover" @click.native="$router.push('/activity/' + a.id)">
            <div class="card-top">
              <el-tag size="mini" type="warning">{{ a.categoryName }}</el-tag>
              <el-tag v-if="a.signedUp" size="mini" type="success">已报名</el-tag>
              <el-tag v-else-if="isFull(a)" size="mini" type="danger">已满员</el-tag>
              <el-tag v-else-if="isExpired(a)" size="mini" type="info">已截止</el-tag>
            </div>
            <h3 class="card-title">{{ a.title }}</h3>
            <p class="card-line"><i class="el-icon-location-outline"></i> {{ a.location }}</p>
            <p class="card-line"><i class="el-icon-time"></i> {{ a.startTime }}</p>
            <div class="card-bottom">
              <span class="signed-info">
                已报 <b>{{ a.signedCount }}</b> / {{ a.maxPeople }} 人
              </span>
              <el-button type="text" @click.stop="$router.push('/activity/' + a.id)">查看详情</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="pagination-wrap" v-if="total > 0">
      <el-pagination
        background
        layout="prev, pager, next, total"
        :total="total"
        :page-size="query.size"
        :current-page.sync="query.page"
        @current-change="loadActivities"
      />
    </div>
  </div>
</template>

<script>
import { listActivities, listCategories } from '@/api';

export default {
  name: 'ActivityList',
  data() {
    return {
      loading: false,
      categories: [],
      activities: [],
      total: 0,
      query: { keyword: '', categoryId: null, page: 1, size: 8 }
    };
  },
  created() {
    this.loadCategories();
    this.loadActivities();
  },
  methods: {
    loadCategories() {
      listCategories().then(data => {
        this.categories = data || [];
      });
    },
    loadActivities() {
      this.loading = true;
      listActivities(this.query)
        .then(data => {
          this.activities = data.records || [];
          this.total = Number(data.total || 0);
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleSearch() {
      this.query.page = 1;
      this.loadActivities();
    },
    isFull(a) {
      return a.remainCount === 0 && !a.signedUp;
    },
    isExpired(a) {
      return new Date(a.signupDeadline).getTime() < Date.now();
    }
  }
};
</script>

<style scoped>
.search-card {
  margin-bottom: 16px;
}
.activity-card {
  margin-bottom: 16px;
  cursor: pointer;
}
.card-top {
  margin-bottom: 8px;
}
.card-title {
  font-size: 16px;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.card-line {
  color: #909399;
  font-size: 13px;
  margin-bottom: 4px;
}
.card-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
}
.signed-info {
  color: #606266;
  font-size: 13px;
}
.signed-info b {
  color: #409eff;
}
.pagination-wrap {
  text-align: center;
  margin: 16px 0 8px;
}
</style>
