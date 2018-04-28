<template>
  <div>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">分店管理</Breadcrumb-item>
      <Breadcrumb-item href="/inns">分店列表</Breadcrumb-item>
      <Breadcrumb-item>分店{{ id ? '编辑' : '新增' }}</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
        <Form-item label="分店编码" prop="innCode">
          <Row>
            <Col span="12">
              <Input v-if="id" v-model="formValidate.innCode" placeholder="请输入分店编码" disabled></Input>
              <Input v-if="!id" v-model="formValidate.innCode" placeholder="请输入分店编码"></Input>
            </Col>
          </Row>
        </Form-item>
        <Form-item label="分店名称" prop="innName">
          <Row>
            <Col span="12">
              <Input v-model="formValidate.innName" placeholder="请输入分店名称"></Input>
            </Col>
          </Row>
        </Form-item>
        <Form-item label="经度" prop="latitude">
          <Row>
            <Col span="12">
              <Input v-model="formValidate.latitude" placeholder="请输入经度"></Input>
            </Col>
          </Row>
        </Form-item>
        <Form-item label="纬度" prop="longitude">
           <Row>
            <Col span="12">
              <Input v-model="formValidate.longitude" placeholder="请输入纬度"></Input>
            </Col>
          </Row>
        </Form-item>
        <Form-item> <Row>
            <Col span="12">
              <Button type="primary" @click="handleSave" class="margin-right-sm">保存</Button>
              <Button type="ghost" @click="$router.push('/inns')">返回</Button>
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
      this.id && this.get(this.id)
    },
    data () {
      return {
        platformgroup: [],
        id: '',
        formValidate: {
          innCode: '',
          innName: '',
          latitude: 0,
          longitude: 0
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
          innName: [
            {
              required: true,
              type: 'string',
              message: '分店名称不能为空'
            },
            {
              max: 100,
              message: '分店名称不能多于 100 个字'
            }
          ],
          latitude: [
            {
              required: true,
              type: 'number',
              message: '经度需要是数字',
              transform(value) {
                return Number(value);
              }
            }
          ],
          longitude: [
            {
              required: true,
              type: 'number',
              message: '纬度需要是数字',
              transform(value) {
                return Number(value);
              }
            }
          ]
        }
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getInn', {uri})
      },
      handleSave () {
        let _this = this;
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putInn' : 'postInn'
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
      'inn'
    ]),
    watch: {
      'inn.inn': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
        }
      }
    }
  }
</script>
