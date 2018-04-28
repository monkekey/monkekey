<template>
  <div>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">服务设置</Breadcrumb-item>
      <Breadcrumb-item href="/services">服务列表</Breadcrumb-item>
      <Breadcrumb-item>账号{{ id ? '编辑' : '新增' }}</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Row>
        <Col span="12">
          <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
            <FormItem label="服务名称" prop="serviceName">
                  <Input v-model="formValidate.serviceName" placeholder="请输入名称"></Input>
            </FormItem>
            <FormItem label="服务描述" prop="serviceDesc">
                  <Input v-model="formValidate.serviceDesc" placeholder="请输入描述"></Input>
            </FormItem>
            <FormItem label="排序" prop="sort">
                  <Input v-model="formValidate.sort" placeholder="请输入排序"></Input>
            </FormItem>
            <Form-item>
                  <Button type="primary" @click="handleSave" class="margin-right-sm">保存</Button>
                  <Button type="ghost" @click="$router.back(-1)">返回</Button>
            </Form-item>
          </Form>
        </Col>
        <Col span="12">
           <img class="avatar" v-bind:src="formValidate.serviceIconUrl" >
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
        uploadparam: {'fileType': 'service'},
        channels: consts.CHANNEL,
        platformgroup: [],
        id: '',
        formValidate: {
          serviceName: '',
          serviceDesc: '',
          serviceUrl: '',
          serviceIconUrl: '',
          sort: 0
        },
        ruleValidate: {
          serviceName: [
            {
              required: true,
              message: '服务名称不能为空'
            },
            {
              max: 20,
              message: '服务名称不能多于 20 个字'
            }
          ],
          serviceDesc: [
            {
              required: true,
              message: '服务描述不能为空'
            },
            {
              max: 100,
              message: '服务描述不能多于 100 个字'
            }
          ],
          sort: [
            {
              required: true,
              type: 'number',
              message: '排序需要是数字',
              transform(value) {
                return parseInt(value);
              }
            }
          ]
        }
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getService', {uri})
      },
      handleSave () {
        let _this = this;
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putService' : 'postService'
            const uri = _this.id

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
              this.$set(this.formValidate, 'serviceUrl', res.data)
              this.$set(this.formValidate, 'serviceIconUrl', consts.STATIC_URL+'/service/'+res.data);
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
      'service'
    ]),
    watch: {
      'service.service': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
          if (newVal.data.serviceUrl) {
            this.$set(this.formValidate, 'serviceIconUrl', consts.STATIC_URL + '/service/' + newVal.data.serviceUrl);
          }
        }
      }
    }
  }
</script>


<style lang="scss">
.avatar {
    width: 200px; 
    height: 200px;
    background: #ffffff;
    border-radius: 2px;
    }
</style>
