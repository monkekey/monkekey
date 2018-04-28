<template>
  <div class="main">
    <EHeader></EHeader>
    <div class="maincontant">
      <Sidebar ref="sidebar"></Sidebar>
      <EBody></EBody>
    </div>
  </div>
</template>

<script>
  import Sidebar from './components/Sidebar'
  import EHeader from './components/Header'
  import EBody from './components/Body'

  export default {
    name: 'layout',
    components: {
      Sidebar,
      EHeader,
      EBody
    },
    beforeRouteUpdate (to, from, next) {
      this.$nextTick(() => {
        this.$refs.sidebar.update(to)
      })
      next()
    }
  }
</script>


<style lang="scss" scoped src="./theme/styles/index.scss">
</style>
