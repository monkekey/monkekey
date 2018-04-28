<template>
  <div class="sidebar">
    <Menu ref="menu" :active-name="activeName" width="auto" style="height:100%" :open-names="openNames" @on-select="handleSelect">

        <Menu-item name="/"> 
          <Icon type="home" size="20"></Icon>
          后台首页
        </Menu-item>
        <Menu-item name="/inns"> 
          <Icon type="ios-list-outline" size="20"></Icon>
          分店管理
        </Menu-item>
        <Menu-item name="/rooms"> 
          <Icon type="ios-grid-view" size="20"></Icon>
          房间管理
        </Menu-item>
        <Menu-item name="/accounts"> 
          <Icon type="person" size="20"></Icon>
          账号管理
        </Menu-item>
        <Menu-item name="/services"> 
          <Icon type="android-happy" size="20"></Icon>
          服务配置
        </Menu-item>
        <Menu-item name="/dicttypes"> 
          <Icon type="android-happy" size="20"></Icon>
          商品类型
        </Menu-item>
        <Menu-item name="/dicts"> 
          <Icon type="android-happy" size="20"></Icon>
          商品管理
        </Menu-item>
        <Menu-item name="/goodses"> 
          <Icon type="android-happy" size="20"></Icon>
          门店商品
        </Menu-item>
        <Menu-item name="/orders"> 
          <Icon type="android-happy" size="20"></Icon>
          订单管理
        </Menu-item>
    </Menu>
  </div>
</template>

<script>
  export default {
    name: 'sidebar',
    data () {
      return {
        activeName: '',
        openNames: []
      }
    },
    created () {
      this.update()
    },
    methods: {
      handleSelect (name) {
        this.$router.push(name)
      },
      update (route) {
        const path = route ? route.path : this.$route.path
        const openName = path.split('/')[1]
        const activeName = '/' + openName

        this.$set(this, 'activeName', activeName)
        this.$set(this, 'openNames', [openName])

        this.$nextTick(() => {
          this.$refs.menu.updateActiveName()
          this.$refs.menu.$children.forEach((item) => {
            item.opened = false
          })
          this.$refs.menu.updateOpened()
        })
      }
    }
  }
</script>

<style lang="scss" scoped src="./theme/styles/index.scss">
</style>
