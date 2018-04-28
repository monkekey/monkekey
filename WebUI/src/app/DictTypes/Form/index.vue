<template>
  <div>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">商品类型管理</Breadcrumb-item>
      <Breadcrumb-item href="/dicttypes">商品类型列表</Breadcrumb-item>
      <Breadcrumb-item>商品类型{{ id ? '编辑' : '新增' }}</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Row>
        <Col span="12">
          <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="120">
            <FormItem label="类型名称" prop="typeName">
                  <Input v-model="formValidate.categoryName" placeholder="请输入类型名称"></Input>
            </FormItem>
            <FormItem label="是否系统内置" prop="isFixed">
                  <RadioGroup v-model="formValidate.categoryIsFixed">
                    <Radio label="1" >是</Radio>
                    <Radio label="0" >否</Radio>
                  </RadioGroup>
            </FormItem>
            <Form-item>
                  <Button type="primary" @click="handleSave" class="margin-right-sm">保存</Button>
                  <Button type="ghost" @click="$router.back(-1)">返回</Button>
            </Form-item>
          </Form>
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
      this.id && this.get(this.id)
    },
    data () {
      return {
        id: '',
        formValidate: {
          categoryName: '',
          categoryIsFixed: 0
        },
        ruleValidate: {
          
          categoryName: [
            {
              required: true,
              message: '类型名称不能为空'
            },
            {
              max: 20,
              message: '类型名称不能多于 20 个字'
            }
          ]
        }
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getDictType', {uri})
      },
      handleSave () {
        let _this = this;
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putDictType' : 'postDictType'
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
      resetFields () {
        this.$refs.formValidate.resetFields()
      }
    },
    computed: mapState([
      'dicttype'
    ]),
    watch: {
      'dicttype.dicttype': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
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
