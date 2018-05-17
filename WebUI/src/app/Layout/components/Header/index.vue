<template>
  <div class="header">
    <div class="header-center">
      <img src="static/images/hpd.png" class="logo" style="width: 120px;height: auto;">
      <!-- <Button type="ghost" size="large" icon="social-windows" @click="$router.push('/')">后台管理系统</Button> -->
      <div class="operations">
        <Button-group size="small">
          <Button type="ghost" icon="android-person">当前用户：admin</Button>
          <Button type="ghost" icon="log-out" @click="handleLogout">退出</Button>
        </Button-group>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'header',
    methods: {
      handleLogout () {
        this.$router.push('/logout')
        this.$Message.success('退出成功')
      }
    }
  }
</script>

<style lang="scss" scoped src="./theme/styles/index.scss">
</style>
