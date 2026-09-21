<template>
  <div>
    <el-card shadow="never">
      <div class="toolbar">
        <div class="spacer"></div>
        <el-button type="primary" icon="el-icon-plus" @click="openDialog()">新增分类</el-button>
      </div>

      <el-table v-loading="loading" :data="categories" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" min-width="200" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="160">
          <template slot-scope="{ row }">
            <el-button type="text" @click="openDialog(row)">编辑</el-button>
            <el-button type="text" style="color: #f56c6c" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && categories.length === 0" description="暂无分类" />
    </el-card>

    <el-dialog :title="form.id ? '编辑分类' : '新增分类'" :visible.sync="dialog" width="420px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="如：文体活动、学术讲座" maxlength="20" />
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
import { listCategories, addCategory, updateCategory, deleteCategory } from '@/api';

export default {
  name: 'CategoryManage',
  data() {
    return {
      loading: false,
      saving: false,
      categories: [],
      dialog: false,
      form: { id: null, name: '' },
      rules: {
        name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.loadCategories();
  },
  methods: {
    loadCategories() {
      this.loading = true;
      listCategories()
        .then(data => {
          this.categories = data || [];
        })
        .finally(() => {
          this.loading = false;
        });
    },
    openDialog(row) {
      this.form = row ? { id: row.id, name: row.name } : { id: null, name: '' };
      this.dialog = true;
    },
    submit() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return;
        this.saving = true;
        const req = this.form.id ? updateCategory(this.form.id, this.form) : addCategory(this.form);
        req
          .then(() => {
            this.$message.success('保存成功');
            this.dialog = false;
            this.loadCategories();
          })
          .finally(() => {
            this.saving = false;
          });
      });
    },
    handleDelete(row) {
      this.$confirm(`确定删除分类「${row.name}」吗？`, '删除确认', { type: 'warning' })
        .then(() => {
          deleteCategory(row.id).then(() => {
            this.$message.success('删除成功');
            this.loadCategories();
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
</style>
