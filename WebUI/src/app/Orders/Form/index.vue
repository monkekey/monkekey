<template>
  <div>
    <Modal
      width="300"
      v-model="del.modal"
      title="确认框"
      @on-ok="handleDelOk">
      <p>确认删除该记录？</p>
    </Modal>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">字典管理</Breadcrumb-item>
      <Breadcrumb-item href="/dicts">字典列表</Breadcrumb-item>
      <Breadcrumb-item>字典{{ id ? '编辑' : '新增' }}</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Row>
        <Col span="16">
          <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="120">
            <FormItem v-if="id" label="订单号" prop="orderNo">
                  <Input v-model="formValidate.orderNo" disabled></Input>
            </FormItem>
            <FormItem label="门店" prop="innCode">
                  <Select
                    v-model="formValidate.innCode"
                    placeholder="请选择输入门店"
                    filterable
                    disabled>
                    <Option v-for="item in inn.allinns" :value="item.code" :key="item.code">{{item.name}}</Option>
                  </Select>
            </FormItem>
            <FormItem label="客人" prop="guestName">
                  <Input v-model="formValidate.guestName" disabled></Input>
            </FormItem>
            <FormItem label="订单金额" prop="amount">
                  <Input v-model="formValidate.amount" disabled></Input>
            </FormItem>
            <FormItem label="备注" prop="remark">
                   <Input v-model="formValidate.remark" type="textarea" :autosize="{minRows: 2,maxRows: 5}" disabled></Input>
            </FormItem>
            <FormItem label="商品列表">
                    <Table :columns="goods_columns" :data="formValidate.orderDetails"></Table>
            </FormItem>
            <FormItem label="取消原因" prop="delRemark">
                   <Input v-model="formValidate.delRemark" type="textarea" :autosize="{minRows: 2,maxRows: 5}"></Input>
            </FormItem>
            <Form-item>
                  <!-- <Button type="primary" @click="handleSave" class="margin-right-sm">保存</Button> -->
                  <Button type="warning" @click="handleDel" class="margin-right-sm">取消</Button>
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
      if (this.inn.allinns.length<=0) {
          let uri = 'all';
          this.$store.dispatch('getAllInns', {uri}) 
      }

      this.id = this.$route.params.id
      this.id && this.get(this.id)
    },
    data () {
      return {
        del: {
          modal: false
        },
        id: '',
        formValidate: {
          orderNo: '',
          innCode: '',
          guestName: '',
          amount: 0,
          delRemark: '',
          orderDetails: []
        },
        goods_columns: [
            {
                title: '商品名称',
                key: 'goodsName'
            },
            {
                title: '单价(元)',
                key: 'salePrice'
            },
            {
                title: '数量',
                key: 'qty'
            }
        ],
        ruleValidate: {
          delRemark: [
            {
              required: true,
              message: '取消原因不能为空'
            },
            {
              max: 200,
              message: '取消原因不能多于 200 个字'
            }
          ]
        }
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getOrder', {uri})
      },
      handleSave () {
        let _this = this;
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putOrder' : 'postOrder'
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
      },
      handleDel (id) {
        let _this = this;
         _this.$refs.formValidate.validate((valid) => {
            if (valid) {
              _this.$set(_this.del, 'modal', true);
            }
         });
      },
      handleDelOk () {
        this.$store.dispatch('deleteOrder', {
          params: {
            orderNo: this.formValidate.orderNo,
            delRemark: this.formValidate.delRemark
          }
        }).then(() => {
          this.$Message.success('删除成功！')
          this.$router.back(-1)
        })
      }
    },
    computed: mapState([
      'order',
      'inn'
    ]),
    watch: {
      'order.order': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
          let _this = this;
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
