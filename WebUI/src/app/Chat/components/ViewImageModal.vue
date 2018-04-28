<template>
<modal name="view-image-modal" transition="pop-out" :width="600" :height="480" @before-open="beforeOpen">
  <div class="vmbox">
    <div class="box-part">
        <img v-bind:src="resourceurl">
    </div>
    </div>
  </div>
</modal>
</template>
<script>
const MODAL_WIDTH = 600
export default {
  name: 'ViewImageModal',
  data () {
    return {
      modalWidth: MODAL_WIDTH,
      resourceurl: ''
    }
  },
  methods: {
    beforeOpen (event) {
      //console.log(event.params.resourceurl);
      this.resourceurl = event.params.resourceurl;
    }
  }
}
</script>
<style lang="less">
.vmbox {
  background: white;
  overflow: hidden;
  width: 600px;
  height: 480px;
  border-radius: 2px;
  box-sizing: border-box;
  box-shadow: 0 0 40px black;
  color: #8b8c8d;
  font-size: 0;

  .box-part {
    display: flex;
    vertical-align: middle;
    box-sizing: border-box;
    height: 100%;
    width: 100%;
    justify-content: center;
    align-items: center;

    img {
      max-width: 600px;
      max-height: 480px;
    }
  }
}
</style>