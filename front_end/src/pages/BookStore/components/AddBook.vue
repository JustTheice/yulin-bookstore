
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
    <Uploader ref="uploaderRef" :defaultUrl="form.bookCover && BASE_URL + form.bookCover" :updater="(file) => form.file = file" />
  </el-form-item>
  <el-button type="primary" @click="onSubmit">提交</el-button>
  <!-- 封面上传-end -->
</el-form>
</template>

<script>
import Uploader from '../../../components/Uploader.vue';

import {addBook, editBook} from '../../../api';
import {BASE_URL} from '../../../api/common';
import { computed } from '@vue/runtime-core';

  export default {
    name: 'AddBook',
    props:['updateBooks', 'mode', "book"],
    components: {
      Uploader
    },
    data(){
      return {
        BASE_URL
      }
    },
    computed: {
      form(){
        return this.book;
      }
    },
    methods: {
      onSubmit() {
        const {form} = this;
        if(!form.bookName){
          return this.$message({
            type: 'info',
            message: '书名不能为空'
          });
        }
        if(form.price<0 || form.surplus<0){
          return this.$message({
            type: 'info',
            message: '价格和数量必须>=0'
          });
        }
        //表单数据
        const formData = new FormData();
        for (const key in form) {
          formData.append(key, form[key]);
        }
        //请求模式处理
        let reqFn;
        switch (this.mode) {
          case "add":
            reqFn = addBook;
            break;
          case "edit":
            reqFn = editBook;
            formData.append('id', this.book.id)
            break;
        }

        console.log(form);

        reqFn(formData).then(
          res => {
            let type = 'info';
            if(res.data.code === 0){
              type = 'success';
              this.$emit('edit-over', this.mode ,res.data.data);
            }else if (res.data.code === 5){
              type = 'error';
            }
            this.$message({
              type,
              message:res.data.msg
            })
          }
        );
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
