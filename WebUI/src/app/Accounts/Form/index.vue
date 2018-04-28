<template>
  <div>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">账号管理</Breadcrumb-item>
      <Breadcrumb-item href="/accounts">账号列表</Breadcrumb-item>
      <Breadcrumb-item>账号{{ id ? '编辑' : '新增' }}</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Row>
        <Col span="12">
          <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
            <FormItem label="账号" prop="account">
                  <Input v-if="id" v-model="formValidate.account" placeholder="请输入账号" disabled></Input>
                  <Input v-if="!id" v-model="formValidate.account" placeholder="请输入账号"></Input>
            </FormItem>
            <FormItem label="名称" prop="name">
                  <Input v-model="formValidate.name" placeholder="请输入名称"></Input>
            </FormItem>
            <FormItem label="所属分店" prop="innCode">
                  <Select
                    v-model="formValidate.innCode"
                    placeholder="请选择输入门店"
                    filterable>
                    <Option v-for="item in inn.allinns" :value="item.code" :key="item.code">{{item.name}}</Option>
                </Select>
            </FormItem>
            <FormItem label="性别" prop="sex">
                  <RadioGroup v-model="formValidate.sex">
                    <Radio label="M" >男</Radio>
                    <Radio label="F" >女</Radio>
                  </RadioGroup>
            </FormItem>
            <FormItem label="出生年月" prop="birthDay">
                  <DatePicker type="date" placeholder="选择日期" v-model="formValidate.birthDay"></DatePicker>
            </FormItem>
            <FormItem label="手机" prop="mobile">
                  <Input v-model="formValidate.mobile" placeholder="请输入手机"></Input>
            </FormItem>
            <FormItem label="性格" prop="nature">
                  <Input v-model="formValidate.nature" placeholder="请输入性格"></Input>
            </FormItem>
            <FormItem label="特长" prop="specialty">
                  <Input v-model="formValidate.specialty" placeholder="请输入特长"></Input>
            </FormItem>
            <Form-item>
                  <Button type="primary" @click="handleSave" class="margin-right-sm">保存</Button>
                  <Button type="ghost" @click="$router.back(-1)">返回</Button>
            </Form-item>
          </Form>
        </Col>
        <Col span="12">
           <img class="avatar" v-bind:src="formValidate.facesurl" >
           <Upload :action="uploadurl" accept="image/*" 
                    name="uploadingFile"
                    :data="uploadparam"
                    :max-size="10240" 
                    :on-success="uploadSuccess" 
                    :show-upload-list="false" >
              <Button type="ghost" icon="ios-cloud-upload-outline">上传头像</Button>
          </Upload>
        </Col>
      </Row>
    </div>
  </div>
</template>

<script>
  import { mapState } from 'vuex'
  import consts from '@/utils/consts'

  export default {
    name: 'form',
    created () {
      this.id = this.$route.params.id
      this.formValidate.innCode = this.$route.query.innCode || ''
      this.id && this.get(this.id)
    },
    data () {
      return {
        uploadurl : consts.API_URL + '/file/uploadFile',
        uploadparam: {'fileType': 'userHead'},
        channels: consts.CHANNEL,
        platformgroup: [],
        id: '',
        formValidate: {
          account: '',
          name: '',
          innCode: '',
          faces: '',
          facesurl: 'static/images/yumi_logo.png',
          sex: 'F',
          birthDay: '',
          mobile: '',
          nature: '',
          specialty: ''
        },
        ruleValidate: {
          account: [
            {
              required: true,
              message: '账号不能为空'
            },
            {
              max: 20,
              message: '标题不能多于 20 个字'
            }
          ],
          name: [
            {
              required: true,
              message: '姓名不能为空'
            },
            {
              max: 20,
              message: '密码不能多于 20 个字'
            }
          ],
          innCode: [
            {
              required: true,
              message: '所属分店不能为空'
            },
            {
              max: 20,
              message: '所属分店不能多于 20 个字'
            }
          ],
          sex: [
             { required: true, type: 'string', message: '请选择性别', trigger: 'change' }
          ],
          birthDay: [
             { required: true, type: 'date', message: '请选择日期', trigger: 'change' }
          ]
        }
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getAccount', {uri})
      },
      handleSave () {
        let _this = this;
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putAccount' : 'postAccount'
            const uri = _this.id
            _this.formValidate.sysroleList = [];
            _this.$store.dispatch(action, {
              uri,
              data: _this.formValidate
            }).then(() => {
              _this.$Message.success((_this.id ? '编辑' : '新增') + '成功！')
              !_this.id && _this.resetFields()
            })
          }
        })
      },
      uploadSuccess: function(res, file){
          //res: {success: true, errMsg: "", data: "171026071357355856910.png"}
          if (res.success) {
              this.$set(this.formValidate, 'faces', res.data)
              this.$set(this.formValidate, 'facesurl', consts.STATIC_URL+ '/userHead/' + res.data);
          }
      },
      checkAllGroupChange (data) {
        this.$set(this.formValidate, 'platform', data.join('GG'))
      },
      resetFields () {
        this.$refs.formValidate.resetFields()
      }
    },
    computed: mapState([
      'account',
      'inn'
    ]),
    watch: {
      'account.account': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
          if (newVal.data.birthDay) {
            this.$set(this.formValidate, 'birthDay', new Date(newVal.data.birthDay))
          }
          if (newVal.data.faces) {
            this.$set(this.formValidate, 'facesurl', consts.STATIC_URL + '/userHead/' + newVal.data.faces);
          }
        }
      }
    }
  }
</script>


<style lang="scss">
.avatar {
    width: 200px; 
    height: 280px;
    background: #ffffff;
    border-radius: 2px;
    }
</style>
