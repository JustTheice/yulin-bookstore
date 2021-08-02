<template>
  <el-row justify="center">
    <el-col>
      <el-result icon="info" title="请先登录" subTitle="请根据提示进行操作">
        <template #extra>
          <el-button type="primary" size="medium" @click="$router.replace('/user/login')">登陆</el-button>
        </template>
      </el-result>
    </el-col>
  </el-row>
</template>

<script>
export default {
  setup () {
    

    return {}
  }
}
</script>

<style lang="scss" scoped>

</style>