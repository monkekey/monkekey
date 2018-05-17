<template>
  <div>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">商品</Breadcrumb-item>
      <Breadcrumb-item href="/dicts">商品列表</Breadcrumb-item>
      <Breadcrumb-item>商品{{ id ? '编辑' : '新增' }}</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Row>
        <Col span="12">
          <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="120">
            <FormItem label="商品类型" prop="categoryCode">
              <Input v-if="id" v-model="formValidate.commodityCategoryCode" disabled></Input>
              <Select v-if="!id" 
                  v-model="formValidate.commodityCategoryCode"
                  placeholder="请选择字典类型"
                  filterable>
                  <Option value="">所有类型</Option>
                  <Option v-for="item in dicttype.alldicttypes" :value="item.categoryCode" :key="item.categoryCode">{{item.categoryName}}</Option>
              </Select>
            </FormItem>
            <FormItem label="商品名称" prop="commodityName">
                  <Input v-model="formValidate.commodityName" placeholder="请输入商品名称"></Input>
            </FormItem>
            <FormItem label="商品概要" prop="commoditySynopsis">
                  <Input v-model="formValidate.commoditySynopsis" placeholder="请输入商品概要"></Input>
            </FormItem>
            <!-- <FormItem label="商品库存" prop="commodityInventory">
                <Row>
                  <Col span="4">
                    <InputNumber :min="0" v-model="formValidate.commodityInventory"></InputNumber>
                  </Col>
                </Row>
            </FormItem> -->
            <FormItem label="单位" prop="commodityUnit">
                  <Input v-model="formValidate.commodityUnit" placeholder="请输入商品单位，如：台"></Input>
            </FormItem>
            <FormItem label="商品进货价" prop="commodityCost">
              <Row>
                <Col span="6">
                  <InputNumber :min="0.01" :formatter="value => `$ ${value}`.replace(/B(?=(d{3})+(?!d))/g, ',')" :parser="value => value.replace(/$s?|(,*)/g, '')" v-model="formValidate.commodityCost"></InputNumber>
                </Col>
                <Col span="2">
                  元
                </Col>
              </Row>
            </FormItem>
            <FormItem label="商品售货价" prop="commodityPrice">
              <Row>
                <Col span="6">
                  <InputNumber :min="0.01" :formatter="value => `$ ${value}`.replace(/B(?=(d{3})+(?!d))/g, ',')" :parser="value => value.replace(/$s?|(,*)/g, '')" v-model="formValidate.commodityPrice"></InputNumber>
                </Col>
                <Col span="2">
                  元
                </Col>
              </Row>
            </FormItem>
            <Form-item>
                  <Button type="primary" :loading="loading" @click="handleSave" class="margin-right-sm">
                      <span v-if="loading">上传中...</span>
                      <span v-else>保存</span>
                      </Button>
                  <Button type="ghost" @click="$router.back(-1)">返回</Button>
            </Form-item>
          </Form>
        </Col>
        <Col span="12">
           <img class="avatar" v-bind:src="formValidate.commodityImg" >
           <Upload :action="uploadurl" accept="image/*" 
                    name="file"
                    :data="updloadData"
                    :max-size="10240" 
                    :on-success="uploadSuccess" 
                    :on-progress="uploadProgress"
                    :on-error="uploadError"
                    :show-upload-list="false" >
              <Button type="ghost" icon="ios-cloud-upload-outline">上传商品图片</Button>
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
      this.id && this.get(this.id)
    },
    data () {
      return {
        id: '',
        formValidate: {
          categoryName: "",
          commodityCategoryCode: "",
          commodityCost: 0.01,
          commodityImg: "",
          // commodityInventory: 1,
          commodityName: "",
          commodityPrice: 0.01,
          commoditySynopsis: "",
          commodityUnit: "",
        },
        ruleValidate: {
          commodityName: [
            {
              required: true,
              message: '商品名称不能为空'
            },
            {
              max: 20,
              message: '商品名称不能多于 20 个字'
            }
          ],
          // commodityInventory: [
          //   {
          //     required: true,
          //     message: '库存不能为空'
          //   },
          //   {
          //     type: 'number',
          //     min: 1,
          //     message: '库存数量不能小于1'
          //   }
          // ],
          commodityPrice: [
            {
              required: true,
              message: '库存不能为空'
            },
            {
              type: 'number',
              min: 0.01,
              message: '进货价不能低于于0.00'
            }
          ],
          commodityCost: [
            {
              required: true,
              message: '库存不能为空'
            },
            {
              type: 'number',
              min: 0.01,
              message: '售货价不能小于0.00'
            }
          ],
          commodityUnit: [
            {
              required: true,
              message: '单位不能为空'
            }
          ]
        },
        uploadurl : consts.API_URL + '/uploadFile/upload',
        updloadData:{
          fileName:'hpd',
          upperFileName:'commodityImg',
          footFileName:''
        },
        loading:false
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getCommodity', {uri})
      },
      handleSave () {
        let _this = this;
        if(_this.formValidate.commodityImg == null || _this.formValidate.commodityImg == ''){
            _this.$Message.error({
              content: '请上传商品图片',
              duration: 5,
              closable: true
            });
            return;
        }
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putCommodity' : 'postCommodity'
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
      uploadSuccess: function(res, file){
        console.log('res',res)
        if (res.success) {
          this.$set(this.formValidate, 'commodityImg', "http://hpd.oss-cn-hangzhou.aliyuncs.com/" + res.data)
          this.loading = false
        }
      },
      uploadProgress(){
        this.loading = true
      },
      uploadError(){
        this.$Message.error({
          content: '上传图片失败，请重试',
          duration: 5,
          closable: true
        });
        this.loading = false
      }
    },
    computed: mapState([
      'dict',
      'commodity',
      'dicttype'
    ]),
    watch: {
      'dict.dict': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
          let _this = this;
          _this.dicttype.alldicttypes.forEach(function (v, i) {
              if (_this.formValidate.categoryCode && v.categoryCode == _this.formValidate.categoryCode) {
                let tt = _this.formValidate;
                tt.typeName = v.typeName
                _this.$set(_this, 'formValidate', tt)
              }
            });
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
