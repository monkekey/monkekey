<template>
  <div>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="/inns">分店管理</Breadcrumb-item>
      <Breadcrumb-item href="/inns">分店列表</Breadcrumb-item>
      <Breadcrumb-item>新增房间</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
        <Form-item label="分店编码" prop="innCode">
          <Row>
            <Col span="12">
              <Select
                  v-model="formValidate.innCode"
                  placeholder="请选择输入门店"
                  filterable>
                  <Option v-for="item in inn.allinns" :value="item.code" :key="item.code">{{item.name}}</Option>
              </Select>
            </Col>
          </Row>
        </Form-item>
        <Form-item label="房间号" prop="roomNo">
          <Row>
            <Col span="12">
              <Input v-model="formValidate.roomNo" type="textarea" :autosize="{minRows: 4, maxRows: 10}" placeholder="请输入房间号"></Input>
              多个房间可用逗号、分号隔开。 
            </Col>
          </Row>
        </Form-item>
        <Form-item label="WIFI 账号" prop="wifiName">
          <Row>
            <Col span="12">
              <Input v-model="formValidate.wifiName" placeholder="请输入房间的WIFI"></Input>
            </Col>
          </Row>
        </Form-item>
        <Form-item label="WIFI 密码" prop="wifiPassword">
          <Row>
            <Col span="12">
              <Input v-model="formValidate.wifiPassword" placeholder="请输入房间的WIFI密码"></Input>
            </Col>
          </Row>
        </Form-item>
        
        <Form-item> <Row>
            <Col span="12">
              <Button type="primary" @click="handleSave" class="margin-right-sm">保存</Button>
              <Button type="ghost" @click="$router.back(-1)">返回</Button>
            </Col>
          </Row>
        </Form-item>
      </Form>
    </div>
  </div>
</template>

<script>
  import { mapState } from 'vuex'

  export default {
    name: 'form',
    created () {
      this.id = this.$route.params.id
      this.formValidate.innCode = this.$route.query.innCode || ''
      
      this.id && this.get(this.id)
    },
    data () {
      return {
        platformgroup: [],
        id: '',
        formValidate: {
          innCode: '',
          roomNo: '',
          wifiName:'',
          wifiPassword:''
        },
        ruleValidate: {
          innCode: [
            {
              required: true,
              type: 'string',
              message: '分店编码不能为空'
            },
            {
              max: 50,
              message: '分店编码不能多于 50 个字'
            }
          ],
          roomNo: [
            {
              required: true,
              type: 'string',
              message: '房间号不能为空'
            },
            {
              max: 100,
              message: '房间号不能多于 1000 个字'
            }
          ]
        }
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getRoom', {uri})
      },
      handleSave () {
        let _this = this;
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putRoom' : 'postRoom'
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
      checkAllGroupChange (data) {
        this.$set(this.formValidate, 'platform', data.join('GG'))
      },
      resetFields () {
        this.$refs.formValidate.resetFields()
      }
    },
    computed: mapState([
      'room',
      'inn'
    ]),
    watch: {
      'room.room': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
        }
      }
    }
  }
</script>
