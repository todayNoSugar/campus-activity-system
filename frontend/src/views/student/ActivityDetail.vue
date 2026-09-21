<template>
  <div v-loading="loading">
    <el-page-header content="活动详情" @back="$router.push('/activity')" />
    <el-card v-if="activity" class="detail-card" shadow="never">
      <el-row :gutter="24">
        <!-- 左侧：活动信息 -->
        <el-col :xs="24" :md="15">
          <div class="detail-title">
            <el-tag type="warning">{{ activity.categoryName }}</el-tag>
            <h2>{{ activity.title }}</h2>
          </div>
          <el-descriptions :column="1" border class="detail-desc">
            <el-descriptions-item label="活动地点">{{ activity.location }}</el-descriptions-item>
            <el-descriptions-item label="开始时间">{{ activity.startTime }}</el-descriptions-item>
            <el-descriptions-item label="结束时间">{{ activity.endTime }}</el-descriptions-item>
            <el-descriptions-item label="报名截止">{{ activity.signupDeadline }}</el-descriptions-item>
            <el-descriptions-item label="人数上限">{{ activity.maxPeople }} 人</el-descriptions-item>
          </el-descriptions>
          <div class="detail-intro">
            <h4>活动简介</h4>
            <p>{{ activity.intro || '暂无简介' }}</p>
            <h4>活动详情</h4>
            <p class="detail-content">{{ activity.detail || '暂无详情' }}</p>
          </div>
        </el-col>
        <!-- 右侧：报名面板 -->
        <el-col :xs="24" :md="9">
          <el-card class="signup-panel" shadow="hover">
            <div class="panel-stats">
              <div class="stat-item">
                <div class="stat-num">{{ activity.signedCount }}</div>
                <div class="stat-label">已报名</div>
              </div>
              <div class="stat-item">
                <div class="stat-num">{{ remainText }}</div>
                <div class="stat-label">剩余名额</div>
              </div>
            </div>
            <el-alert
              v-if="activity.signedUp"
              :title="signedUpText"
              type="success"
              :closable="false"
              class="panel-alert"
            />
            <el-alert
              v-else-if="isExpired"
              title="报名已截止"
              type="info"
              :closable="false"
              class="panel-alert"
            />
            <el-alert
              v-else-if="isFull"
              title="名额已满"
              type="warning"
              :closable="false"
              class="panel-alert"
            />
            <el-button
              type="primary"
              size="medium"
              class="signup-btn"
              :disabled="activity.signedUp || isExpired || isFull"
              :loading="signing"
              @click="handleSignup"
            >
              {{ activity.signedUp ? '已报名' : (isExpired ? '已截止' : (isFull ? '名额已满' : '立即报名')) }}
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { getActivity, signup } from '@/api';

export default {
  name: 'ActivityDetail',
  data() {
    return {
      loading: false,
      signing: false,
      activity: null
    };
  },
  computed: {
    isExpired() {
      if (!this.activity) return false;
      return new Date(this.activity.signupDeadline).getTime() < Date.now();
    },
    isFull() {
      if (!this.activity) return false;
      return this.activity.remainCount === 0;
    },
    remainText() {
      if (!this.activity) return '-';
      return this.activity.remainCount === null ? '不限' : this.activity.remainCount;
    },
    signedUpText() {
      if (!this.activity) return '';
      if (this.activity.status === 1) return '您已报名，请等待管理员审核';
      return '您已报名';
    }
  },
  created() {
    this.loadDetail();
  },
  methods: {
    loadDetail() {
      this.loading = true;
      getActivity(this.$route.params.id)
        .then(data => {
          this.activity = data;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleSignup() {
      this.$confirm('确认报名该活动吗？', '报名确认', { type: 'warning' })
        .then(() => {
          this.signing = true;
          signup(this.activity.id)
            .then(() => {
              this.$message.success('报名成功，请等待管理员审核');
              this.loadDetail();
            })
            .finally(() => {
              this.signing = false;
            });
        })
        .catch(() => {});
    }
  }
};
</script>

<style scoped>
.detail-card {
  margin-top: 16px;
}
.detail-title {
  margin-bottom: 16px;
}
.detail-title h2 {
  margin-top: 8px;
  color: #303133;
}
.detail-desc {
  margin-bottom: 8px;
}
.detail-intro h4 {
  margin: 16px 0 8px;
  color: #303133;
}
.detail-intro p {
  color: #606266;
  line-height: 1.8;
}
.detail-content {
  white-space: pre-wrap;
}
.signup-panel {
  text-align: center;
}
.panel-stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 16px;
}
.stat-num {
  font-size: 26px;
  font-weight: 600;
  color: #409eff;
}
.stat-label {
  color: #909399;
  font-size: 13px;
  margin-top: 4px;
}
.panel-alert {
  margin-bottom: 16px;
}
.signup-btn {
  width: 100%;
}
</style>
