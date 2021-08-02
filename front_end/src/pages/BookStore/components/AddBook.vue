
<template>
<el-form ref="form" :model="form" label-width="80px">
  <el-form-item label="书籍名称">
    <el-input v-model="form.bookName"></el-input>
  </el-form-item>
  <el-form-item label="书籍数量">
    <el-input v-model="form.surplus" type="number"></el-input>
  </el-form-item>
  <el-form-item label="书籍价格">
    <el-input v-model="form.price" type="number"></el-input>
  </el-form-item>
  <!-- 封面上传-start -->
  <el-form-item label="上传封面">
    <Uploader :updater="(file) => form.file = file" />
  </el-form-item>
  <el-button type="primary" @click="onSubmit">提交</el-button>
  <!-- 封面上传-end -->
</el-form>
</template>

<script>
import Uploader from '../../../components/Uploader.vue';

  export default {
    name: 'AddBook',
    components: {
      Uploader
    },
    data() {
      return {
        form: {
          bookName: '',
          price: 0,
          surplus: 0,
          file: {}
        }
      }
    },
    methods: {
      onSubmit() {
        console.log(this.form);
      }
    }
  }
</script>

<style lang="stylus">
.el-form
  .el-button
    display block
    margin 0 auto
</style>
