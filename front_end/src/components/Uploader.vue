
<template>
<el-upload
  class="avatar-uploader"
  :action="action"
  :with-credentials="true"
  :show-file-list="false"
  :on-success="handleAvatarSuccess"
  :before-upload="beforeAvatarUpload"
  :headers="headers"
  :on-progress="uploading"
>
  <div class="mask" v-show="isUploading">
    {{isUploading ? `${uploadPercent}%` : '点我上传'}}
  </div>
  <img v-if="imageUrl" :src="imageUrl" class="avatar">
  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
</el-upload>
</template>

<script>
import {myAxios} from '../api';
  export default {
    name: 'Uploader',
    props: ['updater', 'defaultUrl', 'innerUpload', 'action', 'callback'],
    data() {
      return {
        headers: {
          token: window.localStorage.getItem('token')
        },
        isUploading: false,
        uploadPercent: 0,
        myUrl: ''
      };
    },
    computed: {
      imageUrl(){
        if(this.myUrl){
          console.log(this.myUrl)
          return this.myUrl
        }
        return this.defaultUrl;
      } 
    },
    // mounted(){
    //   console.log(this.action)
    // },
    methods: {
      uploading(e){        
        this.uploadPercent = e.percent.toFixed(1);
      },
      handleAvatarSuccess(res, file) {
        this.isUploading = false;
        this.myUrl = URL.createObjectURL(file.raw);
        this.callback && this.callback(res,file);
      },
      // 图片解析完成，上传前的回掉
      beforeAvatarUpload(file) {
        if(this.isUploading)
          return false;
        
        const isLimitType = file.type === 'image/jpeg' || file.type === 'image/png';
        const isLt10M = file.size / 1024 / 1024 < 10;        

        if (!isLimitType) {
          this.$message.error('只能上传jpg或png格式!');
          return false;
        }
        if (!isLt10M) {
          this.$message.error('上传头像图片大小不能超过 10MB!');
          return false;
        }


        if(this.innerUpload){
          this.isUploading = true;
          return true;
        } else {
          this.myUrl = URL.createObjectURL(file);
          //更新外部form的file
          this.updater && this.updater(file);
        }

        return false;
      }
    }
  }
</script>

<style scoped>
.mask{
  position: absolute;
  width: 178px;
  height: 178px;
  background: rgba(0, 0, 0, .6);
  color: white;
  line-height: 178px;
  text-align: center;
  font-size: 30px;
}
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover .mask{
    display: block!important;
  }
  .avatar-uploader-icon {
    font-size: 25px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
    line-height: 178px!important;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
