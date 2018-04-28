<template>
<modal name="password-modal" transition="pop-out" :width="modalWidth" :height="320">
  <div class="box">
    <div class="box-part" id="bp-left">
      <div class="partition" id="partition-register">
        <div class="partition-title">修改密码</div>
        <div class="partition-form">
          <form autocomplete="false">
            <input id="n-username" v-model="password" type="password" placeholder="输入新密码">
            <input id="n-password2" v-model="password2" type="password" placeholder="重复输入密码">
          </form>

          <div style="margin-top: 42px">
          </div>

          <button class="large-btn github-btn" @click="handleSubmit()">保  存</button>
        </div>
      </div>
    </div>
    </div>
  </div>
</modal>
</template>
<script>
const MODAL_WIDTH = 400
export default {
  name: 'PasswordModal',
  data () {
    return {
      modalWidth: MODAL_WIDTH,
      password: '',
      password2: '',
    }
  },
  created () {
    this.modalWidth = window.innerWidth < MODAL_WIDTH
      ? MODAL_WIDTH / 2
      : MODAL_WIDTH
  },
  methods : {
    handleSubmit() {  
      if (this.password != this.password2){
        this.$Message.error('密码不一致！');
        return;
      }

      if (this.password.length < 6) {
        this.$Message.error('密码长度不能小于6位！');
        return;
      }

      this.$emit('onChangePwd', this.password);
    }
  }
}
</script>
<style lang="less">
.box {
  background: white;
  overflow: hidden;
  width: 400px;
  height: 320px;
  border-radius: 2px;
  box-sizing: border-box;
  box-shadow: 0 0 40px black;
  color: #8b8c8d;
  font-size: 0;

  .box-part {
    display: inline-block;
    position: relative;
    vertical-align: top;
    box-sizing: border-box;
    height: 100%;
    width: 100%;
  }
  
  .box-error-message {
    position: relative;
    overflow: hidden;
    box-sizing: border-box;
    height: 0;
    line-height: 32px;
    padding: 0 12px;
    text-align: center;
    width: 100%;
    font-size: 11px;
    color: white;
    background: #F38181;
  }
  .partition {
    width: 100%;
    height: 100%;
    .partition-title {
      box-sizing: border-box;
      padding: 30px;
      width: 100%;
      text-align: center;
      letter-spacing: 1px;
      font-size: 20px;
      font-weight: 300;
    }
    .partition-form {
      padding: 0 20px;
      box-sizing: border-box;
    }
  }
  input[type=password],
  input[type=text] {
    display: block;
    box-sizing: border-box;
    margin-bottom: 4px;
    width: 100%;
    font-size: 12px;
    line-height: 2;
    border: 0;
    border-bottom: 1px solid #DDDEDF;
    padding: 4px 8px;
    font-family: inherit;
    transition: 0.5s all;
    outline: none;
  }
  button {
    background: white;
    border-radius: 4px;
    box-sizing: border-box;
    padding: 10px;
    letter-spacing: 1px;
    font-family: "Open Sans", sans-serif;
    font-weight: 400;
    min-width: 140px;
    margin-top: 8px;
    color: #8b8c8d;
    cursor: pointer;
    border: 1px solid #DDDEDF;
    text-transform: uppercase;
    transition: 0.1s all;
    font-size: 10px;
    outline: none;
    &:hover {
      border-color: mix(#DDDEDF, black, 90%);
      color: mix(#8b8c8d, black, 80%);
    }
  }
  .large-btn {
    width: 100%;
    background: white;
    span {
      font-weight: 600;
    }
    &:hover {
      color: white !important;
    }
  }
  .github-btn {
    border-color: #82589c;
    color: #82589c;
    &:hover {
      border-color: #82589c;
      background: #82589c;
    }
  }
}
.pop-out-enter-active,
.pop-out-leave-active {
  transition: all 0.5s;
}
.pop-out-enter,
.pop-out-leave-active {
  opacity: 0;
  transform: translateY(24px);
}
</style>