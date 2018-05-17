<template>
    <Card class="login" dis-hover>
      <div class="logo">
          <img class="img" src="static/images/hpd.png" style="width: auto;height: 180px;">
      </div>
      <Form ref="formInline" :model="formInline" :rules="ruleInline" >
        
        <FormItem prop="username">
          <Input type="text" v-model="formInline.username" placeholder="请输入账号">
            <Icon type="ios-person-outline" slot="prepend"></Icon>
          </Input>
        </FormItem>
        <FormItem prop="password">
          <Input type="password" v-model="formInline.password" placeholder="请输入密码">
            <Icon type="ios-locked-outline" slot="prepend"></Icon>
          </Input>
        </FormItem>
        <FormItem>
            <Button type="success" long @click="handleSubmit('formInline')">登  录</Button>
        </FormItem>
      </Form>
    </Card>
</template>

<script>
    import store from '@/vuex/store'
    import getters from '@/vuex/getters'
    import actions from '@/vuex/actions'
    import consts from '@/utils/consts'
    import auth from '@/utils/auth'
    import Model from '@/models/login'

    export default {
        vuex : {
            getters : getters,
            actions : actions
        },
        data () {
          return {
            formInline: {
              username: '',
              password: ''
            },
            ruleInline: {
              username: [
                  { required: true, message: '请填写用户名', trigger: 'blur' }
              ],
              password: [
                  { required: true, message: '请填写密码', trigger: 'blur' },
                  { type: 'string', min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
              ]
            }
          }
        },
        methods : {
          handleSubmit(name) {
            let _this = this;
            this.$refs[name].validate((valid) => {
              if (valid) {
                var url = consts.API_URL + 'auth'
                var formData = JSON.stringify(_this.formInline)

                _this.$Loading.start();

                new Model()
                    .POST({
                      data: formData
                    })
                    .then((response) => {
                      if(response.data.success){
                          _this.$Loading.finish();
                          // localStorage.setItem('lavande_butler_token', response.data.data.token)
                          // localStorage.setItem('lavande_butler_userinfo', JSON.stringify(response.data.data.userinfo))
                          auth.login({'token': response.data.data.token, 'manager': response.data.data.userinfo})  


                          var roleList = (_.map(response.data.data.userinfo.sysroleList, 'role')).toString();
                          if(roleList.length == 0){
                            roleList = 'kefu'
                          }


                          _this.$Message.success('登录成功')
                          if(roleList == 'kefu'){
                            _this.$router.push('/chat');
                          }else{
                            _this.$router.push('/inns');
                          }

                        }else{
                          _this.$Loading.error('error1');
                          _this.$Message.error('表单验证失败!');
                        }
                    })

              } else {
                this.$Message.error('表单验证失败!');
              }
            })
          }
        },

        store: store

    }
</script>

<style lang="less">
    .login {
      background-color: white;
      position: absolute;
      left: 50%;
      top: 250px;
      margin-left: -180px;
      width: 360px;
      paddding: 80px;
      
      .logo {
        display: flex;
        flex-wrap: nowrap;
        flex-direction: row;

        .img {
          display: block;
          width: 60px;
          height: 60px;
          margin: 2px auto 5px;
        }
      }
      

      form {
        margin-top: 10px;
        margin-bottom: 10px;
      }
    }
</style>