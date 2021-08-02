<template>
  <div id="profileWrap" v-if="user.id">
    <div class="profile-left">
      <div class="profile-base">
        <div class="profile-avatar">
          <Uploader :defaultUrl="tempUser.avatar ? BASE_URL+tempUser.avatar : defaultAvatar" class="img-uploader"
              :action="`${BASE_URL}/user/uploadAvatar?userId=1`" :innerUpload="true" :callback="avatarUploaded" />
          <!-- <img :src="tempUser.avatar || defaultAvatar" alt="avatar"> -->
        </div>
        <div>
          <el-input class="name-input" v-model="tempUser.username"></el-input>
          <p class="balance">余额: {{tempUser.balance}}<el-button type="success" @click="$router.replace('/user/wallent')">充值</el-button></p>
        </div>
      </div>
      <div class="profile-context">
        <el-input type="textarea" v-model="tempUser.context"></el-input>
      </div>
      <el-button @click="changeInfo">保存修改</el-button>
    </div>
    <div class="split"></div>
    <div class="profile-right">
      <div class="profile-changepwd">
        <el-button type="primary">修改密码</el-button>
        <div v-show="true" class="content">
          <div>
            <span>旧密码：</span>
            <el-input v-model="pwds.oldPwd"></el-input>
          </div>
          <div>
            <span>新密码：</span>
            <el-input v-model="pwds.newPwd"></el-input>
          </div>
          <div>
            <span>确认新密码：</span>
            <el-input v-model="pwds.cfmNewPwd"></el-input>
          </div>
          <el-button type="success" @click="changePwd">提交</el-button>
        </div>
      </div>
      <div class="profile-others">
        <el-button type="warning" @click="logout">退出登陆</el-button>
        <el-button type="danger" @click="closeAccount">注销账号</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from '@vue/reactivity'
import {h} from 'vue';

import Uploader from '../../../components/Uploader.vue';
import { inject } from '@vue/runtime-core'
import {reqLogout, reqCloseAccount, reqChangeInfo, reqChangePwd, reqLogin} from '../../../api';
import {BASE_URL} from '../../../api/common';
  export default {
    components:{
      Uploader
    },
    setup(){
      const userUpdater = inject('userUpdater');

      const user = inject('user')
      const tempUser = user;

      const pwds = reactive({
        oldPwd: '',
        newPwd: '',
        cfmNewPwd: ''
      });

      return {
        tempUser, pwds, user, userUpdater, BASE_URL,
        defaultAvatar: require('../../../assets/img/avatar.png')
      }
    },
    methods:{
      //头像上传成功
      avatarUploaded(res){
        this.userUpdater(res.data);
        this.$message({
          type:'success',
          message: '上传成功'
        })
      },
      //修改信息
      changeInfo(){
        const {username, context} = this.tempUser;
        
        //请求修改用户信息
        reqChangeInfo({username,context}).then(
          res => {
            console.log(res)
            const data = res.data;
            if(data.code === 0){
              this.$message({
                type: 'success',
                message: '修改成功'
              });
            } else {
              this.$message({
                type: 'error',
                message: data.msg
              })
            }
          }, err => {
            console.log(err);
          }
        )
        //更新本地user信息
        this.userUpdater({...this.user, username, context});
        
      },
      //修改密码
      changePwd(){
        const {pwds} = this;
        if(!pwds.oldPwd || !pwds.newPwd || !pwds.cfmNewPwd){
          return this.$notify({
            title: '格式错误',
            message: h('i', { style: 'color: teal'}, '密码不能为空')
          });
        }
        if(pwds.newPwd !== pwds.cfmNewPwd){
          return this.$notify({
            title: '确认错误',
            message: h('i', { style: 'color: teal'}, '新密码和确认密码不一样')
          });
        }
        //发送请求修改密码
        reqChangePwd(pwds).then(
          res => {
            if(res.data.code === 0){
              pwds.oldPwd = '';
              pwds.newPwd = '';
              pwds.cfmNewPwd = '';
            }
            this.$notify({
                title: res.data.code === 0 ? '成功' : '失败',
                message: h('i', { style: 'color: teal'}, res.data.msg),
                type: res.data.code === 0 ? 'success' : 'error'
              });
            
          }
        )
        
      },
      //退出登录
      logout(){
        // let ret = await reqLogout();
        let vm = this;
        this.$confirm('退出登陆？',  {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //发送请求退出登录
          return reqLogout();
        }).then(res => {
          let message = res.data.msg;
          if(res.data.code === 0){
            vm.userUpdater({});
            vm.$message({
              type: 'success',
              message
            });
          } else {
            vm.$message({
              type: 'error',
              message
            });
          }          
        }).
        catch(() => {
          return;
        });
      },
      //注销账号
      async closeAccount(){
        try {
          await this.$confirm('注销账号后不可找回', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
          })
          await this.$confirm('你真的确定要注销账号吗？', '再次警告', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          })
          let ret = await reqCloseAccount();
          let message = ret.data.msg;
          if(ret.data.code === 0){
            this.userUpdater({});
            this.$message({
              type: 'info',
              message
            });
          } else {
            this.$message({
              type: 'error',
              message
            });
          }
          
        } catch (error) {
          return;
        }
      }
    }
  }
</script>

<style lang="stylus">
#profileWrap
  box-sizing border-box 
  height 100%
  overflow hidden
  .el-button
    border-radius 0
  display flex
  .profile-left
    box-sizing border-box     
    height 100%
    flex 1
    display flex
    flex-direction column
    padding 15px
    padding-right 35px
    .profile-base
      flex 1
      display flex
      box-sizing border-box
      padding-bottom 20px
      .profile-avatar
        flex 2
        .img-uploader
          overflow hidden
          display flex
          justify-content center
          align-items center
          width 160px
          height 160px
          // height width
      >div
        flex 3
        display flex
        flex-direction column
        justify-content center
        .name-input
          font-size 20px
          color #333
          margin-bottom 20px
        .balance
          font-size 22px
          color #555
          display flex
          justify-content center
          align-items center
          .el-button
            margin-left 10px
          
    .profile-context
      flex 3
      box-sizing border-box
      padding-bottom 10px
      >.el-textarea
        height 100%
        textarea
          height 100%
          resize none
  .split
    width 2%
    background url(../../../assets/img/split.png)
    background-size 100% 100%
  .profile-right
    flex 1
    display flex
    flex-direction column
    padding 15px
    padding-left 35px
    .profile-changepwd
      flex 1
      text-align start
      >.el-button
        margin-bottom 10px
      .content
        text-align center
        >div
          display flex
          align-items center
          color #555
          span
            flex 1
            text-align start
          .el-input
            flex 4
        >.el-button
          margin-top 10px
    .profile-others
      flex 1
      border-top 1px solid #CCC
      padding-top 10px
</style>