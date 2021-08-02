<template>
  <el-container id="wrap">
    <el-header>
      <h1>雨林书城</h1>
    </el-header>
    <el-container>
      <el-aside>
        <left-side></left-side>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { defineComponent, provide, reactive, ref } from 'vue'
import LeftSide from './components/LeftSide.vue';
import {reqAutoLogin} from './api';

export default defineComponent({
  components:{
    LeftSide
  },
  setup() {
    let user = ref({
      username: '',
      balance: 0,
      avatar: '',
      context: '',
      isAdmin: false,
      id: 0,
    });
    const userUpdater = ((newUser) => {
      user.value = reactive(newUser);
    })
    
    provide('user', user);
    provide('userUpdater', userUpdater);

    return {user, userUpdater}
  },
  mounted(){
    //自动登陆
    reqAutoLogin().then(
      res => {
        if(res.data.code === 0){
          this.userUpdater(res.data.data);
          this.$message({
            type: 'success',
            message: "自动登陆成功"
          });
          this.$router.replace('/bookstore');
        } else {
          this.$message({
            type: 'info',
            message: "请先登录"
          });
        }
      }
    )
  },
  created(){
    //开启路由守卫
    this.$router.beforeEach((to, from, next) => {
      if(to.path.indexOf('/user') === 0){
        if(to.path === '/user/notlogin' || to.path === '/user/login'){
          next();
        }
        if(!this.user.id){
          return next('/user/notlogin');
        }
      }
      next();
    })
  }
})
</script>


<style lang="stylus">
  #wrap
    display flex
    flex-direction column
    height 100vh
    >.el-header, >.el-footer
      h1
        color #EEE
        font-size 30px
        text-shadow 1px 1px 5px #333
      background-color: #B3C0D1;
      color: #333;
      text-align: center;
      line-height: 60px;
    >.el-container
      display flex
      >.el-aside
        flex 1
        background-color: #D3DCE6;
        color: #333;
        text-align: center;
      >.el-main 
        flex 5
        background-color: #E9EEF3;
        color: #333;
        text-align: center;
</style>
