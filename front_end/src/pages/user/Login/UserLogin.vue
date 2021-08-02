<template>
  <div id="login-wrap">
    <div id="login-content" v-if="!user.id">
      <el-form-item>
        <span>用户名</span>
        <el-input v-model="username"></el-input>
      </el-form-item>
      <el-form-item>
        <span>密码</span>
        <el-input v-model="password"></el-input>
      </el-form-item>
      <div class="login-controler">
        <el-button type="primary" @click="login">登陆</el-button>
        <el-button type="warning" @click="register">注册</el-button>
      </div>
      <p class="login-tip">{{loginTip}}</p>
    </div>
    <el-row v-else justify="center">
      <el-col>
        <el-result icon="success" title="提示" subTitle="您已登陆">
          <template #extra>
            <el-button type="primary" size="medium" @click="$router.replace('/bookstore')">看书</el-button>
            <el-button type="warning" size="medium" @click="logout">注销</el-button>
          </template>
        </el-result>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import {reqRegister, reqLogin} from '../../../api/';
import { inject } from '@vue/runtime-core';
export default {
  setup () {
    const user = inject('user')
    const userUpdater = inject('userUpdater');

    const username = ref('');
    const password = ref('');

    const loginTip = ref('');
    return {username, password, loginTip, userUpdater, user}
  },
  methods:{
    //请求注册
    register(){
      const {username,password} = this;
      if(!username || !password)
        return this.loginTip = '用户名或密码不能为空';

      reqRegister({username,password}).then(
        res => {
          const data = res.data;
          this.loginTip = data.msg;
        }
      )
    },
    //请求登录
    login(){
      const {username,password} = this;
      if(!username || !password)
        return this.loginTip = '用户名或密码不能为空';

      reqLogin({username,password}).then(
        res => {
          console.log(res)
          const data = res.data;
          if(data.code === 0){ //登陆成功
            this.$router.replace('/bookstore');
            this.userUpdater(data.data.user);

            //缓存token
            window.localStorage.setItem('token', data.data.token);
            this.$message({
              type: 'success',
              message: "登陆成功"
            });
          }
          this.loginTip = data.msg
        }
      )
    },
    //注销
    logout(){
      this.userUpdater({});
    }
  }
}
</script>

<style lang="stylus">
#login-wrap
  display flex
  justify-content center
  align-items center
  // background blue
  height 100%
  #login-content
    display flex
    flex-direction column
    justify-content center
    align-items center
    border-radius 10px
    width 50%
    padding 20px
    background #EE 
    .el-form-item
      .el-form-item__content
        display flex
        // width 80%
        color black
        span
          flex 1
        .el-input
          flex 3
    p
      margin-top 5px
      color #555
</style>