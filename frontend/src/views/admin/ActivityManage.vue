<template>
  <div>
    <el-card shadow="never">
      <div class="toolbar">
        <el-input v-model="query.keyword" placeholder="按活动名称搜索" clearable style="width: 220px" @keyup.enter.native="handleSearch" @clear="handleSearch" />
        <el-select v-model="query.categoryId" placeholder="全部分类" clearable style="width: 160px; margin-left: 12px" @change="handleSearch">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 12px" @click="handleSearch">搜索</el-button>
        <div class="spacer"></div>
        <el-button type="primary" icon="el-icon-plus" @click="openDialog()">发布新活动</el-button>
      </div>

      <el-table v-loading="loading" :data="activities" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="活动名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="location" label="地点" min-width="120" show-overflow-tooltip />
        <el-table-column prop="startTime" label="开始时间" width="160" />
        <el-table-column prop="signupDeadline" label="报名截止" width="160" />
        <el-table-column label="报名" width="110">
          <template slot-scope="{ row }">
            {{ row.signedCount }} / {{ row.maxPeople }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template slot-scope="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '已发布' : '已下架' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" @click="openDialog(row)">编辑</el-button>
            <el-button type="text" @click="handleToggleStatus(row)">
              {{ row.status === 1 ? '下架' : '发布' }}
            </el-button>
            <el-button type="text" style="color: #f56c6c" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && activities.length === 0" description="暂无活动" />

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
    </el-card>

    <!-- 新增/编辑活动 -->
    <el-dialog :title="form.id ? '编辑活动' : '发布新活动'" :visible.sync="dialog" width="640px" top="6vh">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="活动名称" prop="title">
          <el-input v-model="form.title" maxlength="50" />
        </el-form-item>
        <el-form-item label="活动分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" maxlength="50" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="报名截止" prop="signupDeadline">
          <el-date-picker v-model="form.signupDeadline" type="datetime" placeholder="选择报名截止时间" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%" />
        </el-form-item>
        <el-form-item label="人数上限" prop="maxPeople">
          <el-input-number v-model="form.maxPeople" :min="1" :max="9999" />
        </el-form-item>
        <el-form-item label="活动简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" :rows="2" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="活动详情" prop="detail">
          <el-input v-model="form.detail" type="textarea" :rows="4" maxlength="2000" />
        </el-form-item>
        <el-form-item label="发布状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" active-text="发布" inactive-text="下架" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { adminListActivities, listCategories, addActivity, updateActivity, updateActivityStatus, deleteActivity } from '@/api';

export default {
  name: 'ActivityManage',
  data() {
    return {
      loading: false,
      saving: false,
      categories: [],
      activities: [],
      total: 0,
      query: { keyword: '', categoryId: null, page: 1, size: 10 },
      dialog: false,
      form: this.emptyForm(),
      rules: {
        title: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
        location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
        signupDeadline: [{ required: true, message: '请选择报名截止时间', trigger: 'change' }],
        maxPeople: [{ required: true, message: '请设置人数上限', trigger: 'change' }]
      }
    };
  },
  created() {
    this.loadCategories();
    this.loadActivities();
  },
  methods: {
    emptyForm() {
      return {
        id: null,
        title: '',
        categoryId: null,
        location: '',
        startTime: '',
        endTime: '',
        signupDeadline: '',
        maxPeople: 100,
        intro: '',
        detail: '',
        status: 1
      };
    },
    loadCategories() {
      listCategories().then(data => {
        this.categories = data || [];
      });
    },
    loadActivities() {
      this.loading = true;
      adminListActivities(this.query)
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
    openDialog(row) {
      if (row) {
        this.form = { ...this.emptyForm(), ...row };
      } else {
        this.form = this.emptyForm();
      }
      this.dialog = true;
    },
    submit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return;
        this.saving = true;
        const req = this.form.id ? updateActivity(this.form.id, this.form) : addActivity(this.form);
        req
          .then(() => {
            this.$message.success('保存成功');
            this.dialog = false;
            this.loadActivities();
          })
          .finally(() => {
            this.saving = false;
          });
      });
    },
    handleToggleStatus(row) {
      const target = row.status === 1 ? 0 : 1;
      const action = target === 1 ? '发布' : '下架';
      this.$confirm(`确定${action}活动「${row.title}」吗？`, '提示', { type: 'warning' })
        .then(() => {
          updateActivityStatus(row.id, target).then(() => {
            this.$message.success(`${action}成功`);
            this.loadActivities();
          });
        })
        .catch(() => {});
    },
    handleDelete(row) {
      this.$confirm(`确定删除活动「${row.title}」吗？该活动下的报名记录将一并删除。`, '删除确认', { type: 'warning' })
        .then(() => {
          deleteActivity(row.id).then(() => {
            this.$message.success('删除成功');
            this.loadActivities();
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
