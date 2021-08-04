<template>
  <el-container id="bookstore">
    <el-header>
      <div class="edit-group">
        <el-button type="primary" @click="handleAdd">添加</el-button>
      </div>
      <div class="searcher">
        <el-input v-model="searchText" placeholder="输入书名以搜索" @input="handleSearch"></el-input>
        <el-button icon="el-icon-search" circle></el-button>
      </div>
    </el-header>
    <el-main>
      <el-row class="book-container">
        <el-col v-for="book in currBooks" :key="book.id" :span="12" :sm="8" :md="6">
          <Book @edit-start="handleEdit" @delete-over="updateBooks" :book=book />
        </el-col>
      </el-row>
    </el-main>
    <el-footer>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="pageSize"
        :total="parseInt(currBookType.length)"
        :current-page="currPage"
        @current-change="changePage"
        >
      </el-pagination>
    </el-footer>
    <!-- 添加图书窗口-start -->
    <el-dialog
      title="提示"
      v-model="dialogVisible"
      width="30%"
      center>
      <AddBook ref="addBookRef" :book="editingBook" @edit-over="updateBooks" :mode="editMode" />
    </el-dialog>
    <!-- 添加图书窗口-end -->
  </el-container>
</template>

<script>
import { ref,reactive } from '@vue/reactivity'
import Book from './components/Book.vue';
import AddBook from './components/AddBook.vue';
import { computed, inject, watch } from '@vue/runtime-core';
import {ElMessage} from 'element-plus';

import {reqBooks} from '../../api';
  export default {
    components:{
      Book, AddBook
    },
    setup(){
      const user = inject('user');

      const dialogVisible = ref(false);
      const showAddBook = () => {
        if(user.value.isAdmin){
          dialogVisible.value = true;
        } else {
          ElMessage({
            type:'info',
            message: '您不是管理员'
          });
        }
      };

      const searchText = ref('');

      let books = ref([]);
      let searchBooks = ref([]);
      const currBookType = ref([]);
      const editingBook = ref({});
      const currBooks = computed(() => {
        return currBookType.value.slice((currPage.value-1)*pageSize.value, currPage.value*pageSize.value);
      })
      const currPage = ref(1);
      const pageSize = ref(8);
      
      //搜索
      const handleSearch = (text) => {
        if(text.length === 0){
          currBookType.value = books.value;
        } else {
          searchBooks.value = books.value.filter((item) => item.bookName.search(text)!=-1);
          currBookType.value = searchBooks.value;
        }
      }
      
      const editMode = ref('add');
      //添加
      const handleAdd = () => {
        showAddBook();
        editMode.value = 'add';
        editingBook.value = reactive({});
      }
      //编辑
      const handleEdit = (book) => {
        showAddBook();
        editMode.value = 'edit';
        editingBook.value = book;
      }
      //监视编辑框的关闭
      const addBookRef = ref()
      watch(dialogVisible, (isShow) => {
        if(!isShow){
          addBookRef.value.$refs.uploaderRef.myUrl = '';
        }
      })

      //子事件完成后的事件
      const updateBooks = (type, book) => {
        if(type === 'add'){
          books.value.push(book);
          dialogVisible.value = false;
        } else if (type === 'delete'){
          books.value.splice(books.value.findIndex((item) => item.id === book.id), 1);
        } else if (type === 'edit'){
          // book.bookCover = book.bookCover + '?' + Date.now();
          books.value.splice(books.value.findIndex((item) => item.id === book.id), 1, book);
          dialogVisible.value = false;
          editingBook.value = {};
        } 
        
        return true;
      }
      const changePage = (page) => {
        currPage.value = page;
      }
      window.onresize = (e) => {
        let windowWidth = e.target.document.documentElement.clientWidth;
        if(windowWidth >= 992){
          pageSize.value = 8;
        } else if (windowWidth >= 768){
          pageSize.value = 6;
        } else {
          pageSize.value = 4;
        }
      }

      //发送请求获取图书列表
      reqBooks().then(
        res => {
          const data = res.data;
          if(data.code === 0){
            books.value = data.data;
            currBookType.value = reactive(books.value);
          } else {
            ElMessage({
              type: 'error',
              message: data.data.msg
            });
          }
        }
      )
      return reactive({
        books, dialogVisible, searchText, user, currBooks, pageSize, currPage,
        changePage, updateBooks, searchBooks, currBookType, handleSearch, showAddBook,
        handleEdit, editMode, handleAdd, editingBook, addBookRef
      });
    },
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