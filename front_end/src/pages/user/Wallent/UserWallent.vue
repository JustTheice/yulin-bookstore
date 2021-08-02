<template>
  <div id="wallentWrap">
    <h4>当前余额：{{user.balance}}元</h4>
    <div>
      <el-input v-model="payMoney" placeholder="请输入充值金额" type="number"
        maxlength="5" step="100" min="0"></el-input>
      <el-button type="success" @click="toPay">充值</el-button>
    </div>
  </div>
</template>

<script>
import { ref } from '@vue/reactivity'
import { inject } from '@vue/runtime-core'
import { ElMessage } from 'element-plus'

import {reqToPay} from '../../../api';

export default {
  setup () {
    const user = inject('user');
    const userUpdater = inject('userUpdater');

    const payMoney = ref(0);
    const toPay = async () => {
      if(payMoney.value == 0)
        return ElMessage({
          type: 'warning',
          message: '充值金额不能为0'
        });

      //请求更新用户信息
      let ret = await reqToPay(payMoney.value);
      //同步本地用户信息
      let data = ret.data;
      if(data.code === 0){
        ElMessage({
          type: 'success',
          message: '充值成功'
        });
        userUpdater({...user.value, balance: user.value.balance+Number(payMoney.value)});
      } else {
        ElMessage({
          type: 'warning',
          message: data.msg
        });
      }
    }

    return {payMoney, user, toPay}
  }
}
</script>

<style lang="stylus">
#wallentWrap
  display flex
  height 100% 
  flex-direction column
  justify-content center
  align-items center
  h4
    font-size 25px
    margin-bottom 15px
  >div
    display flex
    align-items center
    .el-input
      height 40px
      .el-input__inner
        width 300px
        border-radius 0
    .el-button
      height 40px
      border-radius 0
</style>