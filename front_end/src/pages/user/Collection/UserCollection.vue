<template>
  <el-table
    :data="collections"
    stripe
    style="width: 100%">
    <el-table-column
      width="50">
      <template #default="scope">
        <img v-lazy="scope.bookCover">
      </template>
    </el-table-column>
    <el-table-column
      prop="bookName"
      label="书籍信息"
      width="500">
    </el-table-column>
    <el-table-column
      prop="price"
      label="价格">
    </el-table-column>
    <el-table-column
      fixed="right"
      width="150"
      label="操作">
      <template #default="scope">
        <el-button @click="handleToBuy(scope.row)" type="success" size="small">购买</el-button>
        <el-button @click="handleRemove(scope.row)" size="small" type="danger">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <div class="buy-footer">
    <h4>总计<span>￥{{totalPrice}}</span></h4>
    <div class="buy-controler">
      <el-button type="success" style="border-radius:0" @click="handleToBuyAll">
      <i class="el-icon-shopping-cart-1"></i>
      结账
    </el-button>
    <el-button type="danger" style="border-radius:0" @click="handleRemoveAll">
      <i class="el-icon-delete"></i>
      清空购物车
    </el-button>
    </div>
  </div>
</template>

<script>
import { computed, inject, reactive, ref } from '@vue/runtime-core'

import {getCollections, reqToBuy, reqRemoveCollection, reqToBuyAll, reqRemoveAllCollections} from '../../../api';
import {ElMessage} from 'element-plus';
export default {
  setup () {
    const collections = ref([]);
    const userUpdater = inject('userUpdater');
    //获取收藏列表
    getCollections().then(
      res => {
        if(res.data.code === 0){
          collections.value = reactive(res.data.data);
        } else {
          ElMessage({
            type: 'info',
            message: '获取收藏列表失败'
          });
        }
      }
    );

    const totalPrice = computed(() => collections.value.reduce((total,item) => total+item.price, 0))
    
    return {
      collections,
      totalPrice,
      userUpdater
    }
  },
  methods:{
    //请求购买
    handleToBuy(book){
      this.collectionReq(reqToBuy, book);
    },
    //请求删除
    handleRemove(book){
      this.collectionReq(reqRemoveCollection, book)
    },
    //清空购物车
    handleRemoveAll(){
      this.collectionReq(reqRemoveAllCollections)
    },
    //请求购买全部
    handleToBuyAll(){
      this.collectionReq(reqToBuyAll);
    },
    collectionReq(reqFn, book){
      reqFn(book && book.id).then(
        res => {
          let type;
          if(res.data.code === 0){
            type = 'success';
            console.log(reqFn.name)
            //避免直接使用函数名判断，因为在打包时会对函数进行重命名
            // if(reqFn.name == 'reqToBuy' || reqFn.name == 'reqToBuyAll'){
            //   // console.log("用户更新")
            // }
            if(res.data.data.id){
              this.userUpdater(res.data.data);
            }
            if(book){
              this.collections.splice(this.collections.findIndex((item) => item.id===book.id), 1);
            } else {
              this.collections = reactive([])
            }
          }else if(res.data.code === 2 || res.data.code === 3 || res.data.code === 4){
            type = 'warning';
          }else{
            type = 'error';
          }

          this.$message({
            type,
            message: res.data.msg
          })
        }
      );
    }
  }
}
</script>

<style lang="stylus">
  .el-table
    img
      width  100%
  .buy-footer
    margin-top 20px 
    padding 0 5px
    display flex
    justify-content space-between
    h4
      font-size 20px
      span
        color red
      
</style>