
<template>
<el-upload
  class="avatar-uploader"
  :action="action"
  :with-credentials="true"
  :show-file-list="false"
  :on-success="handleAvatarSuccess"
  :before-upload="beforeAvatarUpload"
  :headers="headers"
>
  <img v-if="imageUrl" :src="imageUrl" class="avatar">
  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
</el-upload>
</template>

<script>
  export default {
    name: 'Uploader',
    props: ['updater', 'defaultUrl', 'innerUpload', 'action', 'callback'],
    data() {
      return {
        imageUrl: this.defaultUrl,
        headers: {
          token: window.localStorage.getItem('token')
        }
      };
    },
    mounted(){
      console.log(this.action)
    },
    methods: {
      handleAvatarSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
        this.callback && this.callback(res,file);
      },
      // 图片解析完成，上传前的回掉
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;        

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
          return;
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return;
        }

        if(this.innerUpload){
          return true;
        } else {
          this.imageUrl = URL.createObjectURL(file);
          //更新外部form的file
          this.updater && this.updater(file);
        }

      }
    }
  }
</script>

<style scoped>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 30px;
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
