<template>
  <el-table
    :data="tableData"
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
        <el-button @click="handleClick(scope.row)" type="success" size="small">购买</el-button>
        <el-button size="small" type="danger">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <div class="buy-footer">
    <h4>总计<span>￥{{totalPrice}}</span></h4>
    <div class="buy-controler">
      <el-button type="success" style="border-radius:0">
      <i class="el-icon-shopping-cart-1"></i>
      结账
    </el-button>
    <el-button type="danger" style="border-radius:0">
      <i class="el-icon-delete"></i>
      清空购物车
    </el-button>
    </div>
  </div>
</template>

<script>
import { computed, reactive } from '@vue/runtime-core'
export default {
  setup () {
    
    const tableData  = [{
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
        }
      ];
    console.log(tableData)

    const totalPrice = computed(() => tableData.reduce((total,item) => total+item.price, 0))
    
    return {
      tableData,
      totalPrice
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