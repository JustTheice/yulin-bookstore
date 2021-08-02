<template>
  <div class="book">
    <div class="book-bg">
      <img v-lazy="book.bookCover" alt="书籍封面">
      <div class="mask" @click="collectBook(book.id)">
        <i class="el-icon-star-off"></i>
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
import { reactive, ref } from '@vue/reactivity'
export default {
  name: 'Book',
  props: ['book'],
  methods:{
    collectBook(bookId){
      //发送请求，收藏

      // this.$message({
      //   type: 'success',
      //   message: '收藏成功'
      // });
      this.$message({
        type: 'warning',
        message: '您已收藏'
      });
      // this.$message({
      //   type: 'error',
      //   message: '收藏失败'
      // });
    }
  }
}
</script>

<style lang="stylus">
  $bookWidth = 130px;
  $bookHeight = 165px;

  
  .book
    margin-bottom 30px
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