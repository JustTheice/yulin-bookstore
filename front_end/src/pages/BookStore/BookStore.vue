<template>
  <el-container id="bookstore">
    <el-header>
      <div class="edit-group">
        <el-button type="primary" @click="showAddBook">添加</el-button>
      </div>
      <div class="searcher">
        <el-input v-model="searchText" placeholder="输入书名以搜索"></el-input>
        <el-button icon="el-icon-search" circle></el-button>
      </div>
    </el-header>
    <el-main>
      <el-row class="book-container">
        <el-col v-for="book in books" :key="book.id" :span="12" :sm="8" :md="6">
          <Book :book=book />
        </el-col>
      </el-row>
    </el-main>
    <el-footer>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="8"
        :total="50"
        >
      </el-pagination>
    </el-footer>
    <!-- 添加图书窗口-start -->
    <el-dialog
      title="提示"
      v-model="dialogVisible"
      width="30%"
      center>
      <AddBook/>
    </el-dialog>
    <!-- 添加图书窗口-end -->
  </el-container>
</template>

<script>
import { ref,reactive } from '@vue/reactivity'
import Book from './components/Book.vue';
import AddBook from './components/AddBook.vue';
import { inject } from '@vue/runtime-core';
  export default {
    components:{
      Book, AddBook
    },
    methods:{
      showAddBook(){
        if(this.user.isAdmin){
          this.dialogVisible = true;
        } else {
          this.$message({
            type:'info',
            message: '您不是管理员'
          });
        }
      },
    },
    setup(){
      const user = inject('user');

      const dialogVisible = ref(false);
      const searchText = ref('');

      const books = reactive([
      {
        id: 1,
        bookName: '浪潮之巅',
        surplus: 15,
        bookCover: '',
        price: 100
      },
      {
        id: 2,
        bookName: '苏东坡传',
        surplus: 13,
        bookCover: '',
        price: 80
      },
      {
        id: 3,
        bookName: '力量训练基础dsds',
        surplus: 13,
        bookCover: '',
        price: 60
      },
      {
        id: 3,
        bookName: '力量训练基础dsds',
        surplus: 13,
        bookCover: '',
        price: 60
      },
      {
        id: 3,
        bookName: '力量训练基础dsds',
        surplus: 13,
        bookCover: '',
        price: 60
      },
    ])

    return {
      books, dialogVisible, searchText, user
    }
    }
  }
</script>

<style lang="stylus">
  #bookstore
    height 100%
    .el-header
      display flex
      justify-content space-between
      align-items center
      .searcher
        display flex
        align-items center
        .el-input
          height 40px
        .el-button
          height 40px
          border-radius 0 40% 40% 0
    .el-main
      .book-container
        display flex
</style>