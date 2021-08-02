
<template>
<el-menu
      router
      :uniqueOpened="true"
      default-active="/bookstore"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b">
      <el-submenu index="/user">
        <template #title>
          <i class="el-icon-location"></i>
          <span>用户信息</span>
        </template>
        <div class="avatar">
          <a href="javascript:;" v-if="user.id">
            <img :src="user.avatar ? BASE_URL + user.avatar : defaultAvatar" alt="avatar">
            <div class="mask">
              <span v-if="user.isAdmin" style="background-color:rgb(0,150,255);">管理员</span>
              <span v-else style="background-color:red;">图书会员</span>
            </div>
          </a>
          <router-link to="/user/login" v-else>
            <p>未登录</p>
            <div class="mask unlogin">
              <span>点我登陆</span>
            </div>
          </router-link>
        </div>
        <el-menu-item index="/user/collection">
          <i class="el-icon-star-off"></i>
          <template #title>我的收藏</template>
        </el-menu-item>
        <el-menu-item index="/user/wallent">
          <i class="el-icon-wallet"></i>
          <template #title>钱包</template>
        </el-menu-item>
        <el-menu-item index="/user/profile">
          <i class="el-icon-setting"></i>
          <template #title>设置</template>
        </el-menu-item>
      </el-submenu>
      <el-menu-item index="/bookstore">
        <i class="el-icon-menu"></i>
        <template #title>图书大全</template>
      </el-menu-item>
    </el-menu>
</template>

<script>
import { ref } from '@vue/reactivity';
import { inject } from '@vue/runtime-core';

import {BASE_URL} from '../api/common';

  export default {
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      }
    },
    setup(){
      const user = inject('user');

      return{
        user, BASE_URL,
        defaultAvatar: require('../assets/img/avatar.png')
      }
    }
  }
</script>

<style lang="stylus">
  .avatar
    display flex
    justify-content center
    a
      position relative
      display block
      margin 10px
      overflow hidden
      text-align center
      text-decoration none
      border 3px solid red
      width 100px
      height 100px
      border-radius 50%
      color white 
      p,img
        width 100%
        height 100%
      p
        text-align center
        line-height 100px
      .mask
        width 100%
        height 100%
        position absolute
        top 0
        left 0
        background rgba(0,0,0,0)
        opacity 0
        transition .3s
        span
          position absolute
          z-index 2
          width auto
          height 20px
          white-space nowrap
          font-size 16px
          top 50%
          left 50%
          transform translate(-50%,-50%)
          padding 2px
          border-radius 7px
          opacity .8
      &:hover
        .mask
          opacity 1
          background rgba(0,0,0,.3)
        .unlogin
          background rgba(0,0,0,1)
</style>
