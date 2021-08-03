<template>
  <div class="book">
    <div class="book-bg">
      <img :key="book.bookCover" v-lazy="BASE_URL + book.bookCover" alt="书籍封面">
      <div class="mask" @click="collectBook(book.id)">
        <i class="el-icon-star-off"></i>
      </div>
      <div class="book-operator" v-show="user.isAdmin">
        <el-button type="primary" icon="el-icon-edit" size="mini" @click="startEdit(book)"></el-button>
        <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteBook(book.id)"></el-button>
      </div>
    </div>
    <div class="book-description">
      <h3>{{book.bookName}}</h3>
      <div>
        <span>剩{{book.surplus}}</span>
        <span>售价{{book.price}}</span>
      </div>
    </div>
    
  </div>
</template>

<script>
import {reqCollectBook, deleteBook} from '../../../api';
import {BASE_URL} from '../../../api/common';
import { inject } from '@vue/runtime-core';
export default {
  name: 'Book',
  props: ['book'],
  methods:{
    //开始编辑
    startEdit(book){
      this.$emit('edit-start', book);
    },
    //删除
    async deleteBook(bookId){
      await this.$confirm('确认删除？', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'error'
        })
      deleteBook(bookId).then(
        res => {
          let type = 'info';
          if(res.data.code === 0){
            this.$emit('delete-over', 'delete', this.book);
          }
          this.$message({
            type,
            message: res.data.msg
          })
        }
      );
    },
    //收藏
    collectBook(bookId){
      if(!this.user.id){
        return this.$message({
          type: 'info',
          message: '请先登陆'
        })
      }
      //发送请求，收藏
      reqCollectBook(bookId).then(
        res => {
          console.log(res);
          let resData = res.data;
          let type;
          if(resData.code === 0){
            type = 'success';
          } else if(resData.code === 2) {
            type = 'warning'
          } else {
            type = 'error';
          }

          this.$message({
            type,
            message: resData.msg
          });
        }
      );
    }
  },
  setup(){
    const user = inject('user');

    return {
      BASE_URL, user
    }
  }
}
</script>

<style lang="stylus">
  $bookWidth = 130px;
  $bookHeight = 165px;

  
  .book
    position relative
    margin-bottom 15px
    .book-bg
      position relative
      margin 0 auto
      width $bookWidth
      height $bookHeight
      background-image url(../../../assets/img/book-bg.png)
      background-size 100% 100%
      //封面混合
      bookCoverCommon(){
        position absolute
        bottom 1%
        left 5%
        width 95%
        height 98%
      }
      img
        bookCoverCommon()
        z-index 1
      .mask
        bookCoverCommon()
        z-index 2
        opacity 0
        font-size 80px
        color orange
        background black
        line-height $bookHeight - 10px
        cursor pointer
        transition .2s
      &:hover
        .mask
          font-size 110px 
          opacity .8
        .book-operator
          left 105%
      .book-operator
        width 20px
        position absolute
        display flex
        flex-direction column
        align-items center
        top 3%
        left 75%
        transition .3s
        .el-button
          margin 0
          padding 10px
          border-radius 0 3px 3px 0
          &:nth-child(2)
            margin-top 3px
    .book-description
      margin 0 auto
      width $bookWidth
      h3
        flex 3
        font-size 16px
        color #444
        overflow hidden
        white-space: nowrap;
        text-overflow:ellipsis
      >div
        display flex
        align-items center
        justify-content space-between
        padding 5px 0
        span
          flex 1
          font-size 14px
          color #777
    
      
</style>