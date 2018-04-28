<template>
  <div>
    <Modal title="查看" v-model="visible">
        <img :src="imgName" v-if="visible" style="width: 100%">
    </Modal>

    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">门店商品管理</Breadcrumb-item>
      <Breadcrumb-item href="/goodses">门店商品列表</Breadcrumb-item>
      <Breadcrumb-item>门店商品{{ id ? '编辑' : '新增' }}</Breadcrumb-item>
    </Breadcrumb>
    <div>
      <Row>
        <Col span="12">
          <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="120">
            <Form-item label="分店" prop="">
              <Select
                  v-model="hotelId"
                  placeholder="请选择输入门店"
                  filterable
                  @on-change="changeHotel">
                  <Option v-for="item in inn.allinns" :value="item.code" :key="item.code">{{item.name}}</Option>
              </Select>
            </Form-item>
            <FormItem label="商品类型" prop="">
              <Select v-model="categoryIn" filterable :disabled="new_commoditySaveDate.length > 0">
                <Option v-for="item in dicttype.alldicttypes" :value="item.categoryCode" :key="item.categoryCode">{{ item.categoryName }}</Option>
              </Select>
            </FormItem>    
          </Form>
        </Col>
      </Row>
      <Row>
        <Col span="24">
          <Table border :columns="commoditySaveColumns" :data="new_commoditySaveDate" @on-selection-change="selectCommoidty"></Table>
        </Col>
      </Row>
      
      <Row style="margin-top:40px">
        <Col span="24">
          <Button type="primary" @click="handleSave" class="margin-right-sm">保存</Button>
          <Button type="ghost" @click="$router.back(-1)">返回</Button>
        </Col>  
      </Row> 
    </div>
  
    <Modal
        v-model="changeCommodityShow"
        title="选择商品"
        @on-ok="ok"
        @on-cancel="cancel"
        width="1000">
        <Table border ref="selection" :loading="loadingCommity" :columns="commodity" :data="commodityList" @on-selection-change="selectCommoidty"></Table>

        <Row type="flex" justify="center" style="margin-top:20px">
        <Col>
          <Page :total="page" :current="initPage" @on-change="changePage"></Page>
        </Col>
      </Row>
    </Modal>

  </div>
</template>



<script>
  import { mapState } from 'vuex'
  import consts from '@/utils/consts'
  import Commodity from '@/models/commodity'
  import hotelCommodity from '@/models/hotelCommodity'
  export default {
    name: 'form',
    created () {
      console.log('sssss')
      this.id = this.$route.params.id
      console.log('val',this.id)
      // this.id && this.get(this.id)
    },
    data () {
      const validatePrice = (rule, value, callback) => {
          if (Number.isNaN(Number.parseFloat(value))) {
              callback(new Error('请输入数字值'));
          } else {
              callback();
          }
      };

      return {
        id: '',
        uploadurl : consts.API_URL + '/file/uploadFile',
        uploadparam: {'fileType': 'goodsPic'},
        goodsPics: [],
        goodsPicUrls: ['http://img14.360buyimg.com/n1/jfs/t4162/272/378575216/55355/56e7dbca/58b3be82N99650603.jpg'],
        imgName: '',
        visible: false,
        del: {
          modal: false,
          idx: -1
        },
        formValidate: {
          goodsName: '',
          originalPrice: '',
          category: '',
          innCode: '',
          pic: ''
        },
        ruleValidate: {
          goodsName: [
            {
              required: true,
              message: '商品名称不能为空'
            },
            {
              max: 20,
              message: '商品名称不能多于 20 个字'
            }
          ],
          originalPrice: [
            {required: true, message: '售价必须为数字', trigger: 'blur'},
            {validator: validatePrice, trigger: 'blur'}
          ],
          category: [
            {
              required: true,
              message: '商品类别不能为空'
            }
          ],
          innCode: [
            {
              required: true,
              message: '分店不能为空'
            }
          ],
          pic: [
            {
              required: true,
              message: '图片不能为空'
            }
          ]
        },
        categoryIn:[],//选中的商品类型
        loadingCommity:false,
        //查询商品的列
        commodity:[
          {
            type: 'selection',
            width: 60,
            align: 'center'
          },
          {
            title: '类型',
            key: 'categoryName'
          },
          {
            title: '商品名称',
            key: 'commodityName',
            render:(h,params) =>{
              return h('Tooltip',{
                props:{
                  placement:'top'
                }
              },[
                h('a', params.row.commodityName),[
                  h('div',{
                    slot:'content'
                  },[
                    h('img',{
                      attrs:{
                        src: params.row.commodityImg 
                      },
                      style:{
                        width: '120px',
                        height: '120px'
                      }
                    },''),
                  ])
                ]
              ])
            }
          },
          {
            title: '商品概要',
            key: 'commoditySynopsis'
          },
          {
            title: '商品售价',
            key: 'commodityPrice'
          },
          {
            title: '商品进货价',
            key: 'commodityCost'
          }
        ],
        commodityList:[],//查询的商品数据
        page:0,
        initPage:1,
        pageData:{
          pageIdx:0,
          pageSize:10,
        },
        hotelId:'',
        changeCommodity:[],//已选中的商品
        onChangesCommodity:[],//当前页数选中的值
        changeCommodityShow:false,
        commoditySaveDate:[],//保存页数据
        new_commoditySaveDate:[],//需要保存页数据
        //保存页的列
        commoditySaveColumns:[
          {
            title: '类型',
            key: 'categoryName'
          },
          {
            title: '商品名称',
            key: 'commodityName',
            render:(h,params) =>{
              return h('Tooltip',{
                props:{
                  placement:'top'
                }
              },[
                h('a', params.row.commodityName),[
                  h('div',{
                    slot:'content'
                  },[
                    h('img',{
                      attrs:{
                        src: params.row.commodityImg 
                      },
                      style:{
                        width: '120px',
                        height: '120px'
                      }
                    },''),
                  ])
                ]
              ])
            }
          },
          {
            title: '商品概要',
            key: 'commoditySynopsis'
          },
          {
            title: '商品售价',
            key: 'commodityPrice'
          },
          {
            title: '商品进货价',
            key: 'commodityCost'
          },
          {
            title: '商品库存',
            key: 'commodityInventory',
            render:(h,params) => {
                return h('div',[
                  h('InputNumber',{
                    props:{
                      value:params.row.commodityInventory,
                      min:1,
                    },
                    on:{
                      'on-change': (event) => {
                        this.commoditySaveDate[params.index].commodityInventory = event
                        this.$emit('InputNumber',event) 
                      }
                    }
                })
              ])
            }
          },
          {
              title: 'Action',
              key: '',
              width: 90,
              align: 'center',
              fixed: 'right',
              render: (h, params) => {
                  return h('div', [
                      h('Button', {
                          props: {
                              type: 'error',
                              size: 'small',
                              disabled:this.editType
                          },
                          on: {
                              click: () => {
                                  this.removeCommodity(params.index)
                              }
                          }
                      }, '删除')
                  ]);
              },
              renderHeader:(h,params) =>{
                  return h('div',[
                      h('Button',{
                          props:{
                              type:'primary',
                              size:'small',
                              disabled:this.editType
                          },
                          on:{
                              click:() => {
                                  this.addCommodity(params.index)
                              }
                          }
                      },'添加')
                  ])
              }
          }
        ],
      }
    },
    methods: {
      get (uri) {
        this.$store.dispatch('getGoods', {uri})
      },
      handleSave () {
        this.selectOK()
        let _this = this;
        if(null == this.hotelId || "" == this.hotelId){
          this.$Message.error("请选择门店！")
          return
        }
        let hotelCommodityList = this.new_commoditySaveDate
        let new_hotelCommoditys = []
        for(let i in hotelCommodityList){
          new_hotelCommoditys.push(
            {
              hotelId:this.hotelId,
              categoryCode:hotelCommodityList[i].commodityCategoryCode,
              commodityCode:hotelCommodityList[i].commodityCode,
              commodityInventory:hotelCommodityList[i].commodityInventory
            }
          )
        }
        
        let data = {
          hotelId:this.hotelId,
          hotelCategoryList:this.categoryIn,
          hotelCommoditylist:new_hotelCommoditys
        }
        console.log(JSON.stringify(data))
        data = JSON.stringify(data)
        new hotelCommodity().addPath('save').POST({data}).then((res)=>{
          console.log(res)
          if(res.data.data == "ok"){
            this.changeCommodity=[]
            this.onChangesCommodity=[]
            this.new_commoditySaveDate=[]
            
            this.$Message.info('保存成功')

          }else{
            this.$Message.error('保存失败')
          }
        })
        return
        _this.$refs.formValidate.validate((valid) => {
          if (valid) {
            const action = _this.id ? 'putGoods' : 'postGoods'
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
      handleView (item) {
          this.imgName = item;
          this.visible = true;
      },
      resetFields () {
        this.$refs.formValidate.resetFields()
      },
      serch(){
        let _this = this
        _this.loadingCommity = true
        let params = {
          pageIdx:_this.pageData.pageIdx,
          pageSize:_this.pageData.pageSize,
          categoryCodes:_this.categoryIn
        }
        new Commodity().addPath('in').GET({params:params}).then((res) =>{
          if(res.data.success == true){
            _this.page = res.data.data.totalElements
            let result = res.data.data.content;
            let commodityList = result
            if(_this.commoditySaveDate.length > 0){
              for(let e in commodityList){
                for(let f in _this.commoditySaveDate){
                  if(commodityList[e].commodityCode == _this.commoditySaveDate[f].commodityCode){
                    console.log('sss')
                    commodityList[e]._disabled = true;                    
                  }
                }
              }
            }
            if(_this.changeCommodity.length > 0){
                for(let h in commodityList){
                    for(let j in _this.changeCommodity){
                        if(commodityList[h].commodityCode == _this.changeCommodity[j].commodityCode){
                            commodityList[h]._checked = true;
                        }
                    }
                }   
            }
            _this.commodityList = commodityList
            _this.loadingCommity = false
          }else{
            _this.$Message.error('查询失败！')
            _this.loadingCommity = false
          }
        })
      },
      changePage(e){//翻页
        this.selectOK()
        this.initPage = e;
        this.pageData.pageIdx = e-1
        this.serch()
      },
      selectCommoidty(e){
        this.onChangesCommodity = e
        for(let a in this.commodityList){
          this.commodityList[a]._checked = false;
          for(let b in e){
              if(this.commodityList[a].commodityCode == e[b].commodityCode){
                  this.commodityList[a]._checked = true;
              }
          }
        }
      },
      selectOK(){
          for(var i in this.commodityList){
            if(this.commodityList[i]._checked == true){
                this.selectTab = false;
                for(var j in this.changeCommodity){
                    if(this.commodityList[i].commodityCode == this.changeCommodity[j].commodityCode){
                        this.selectTab = true;
                    }
                }
                if(!this.selectTab){
                    this.changeCommodity.push(this.commodityList[i]);
                }
            }else{
                for(var j in this.changeCommodity){
                    if(this.commodityList[i].commodityCode == this.changeCommodity[j].commodityCode){
                        this.changeCommodity.splice(j, 1);
                    }
                }  
            }
        }
      },
      addCommodity(){
        if(this.categoryIn.length == 0){
           this.$Message.error('请先选择商品类型！')
           return
        }
        this.selectOK()
        this.pageData ={
          pageIdx:0,
          pageSize:10,
        },
        this.initPage = 1;
        this.serch()
        this.changeCommodityShow = true
      },
      ok(){
        this.selectOK()
        this.new_commoditySaveDate = this.changeCommodity
      },
      cancel(){
        this.changeCommodity = []
        this.changeCommodityShow = false
      },
      removeCommodity(val){
        console.log(val)
        this.new_commoditySaveDate.splice(val,1)
      },
      changeHotel(){
        console.log(this.hotelId);
        let data = this.hotelId
        new Commodity().addPath('hotelCommodity?hotelID=' + data).GET().then((res) =>{
          console.log(res)
          if(res.data.success == true){
            this.commoditySaveDate = res.data.data
          }
        })
      }
    },
    computed: mapState([
      'goods',
      'inn',
      'dicttype'
    ]),
    watch: {
      'goods.goods': {
        handler (newVal) {
          this.$set(this, 'formValidate', newVal.data)
        }
      },
      'goodsPics':{
        handler (newVal) {
          let t_goodsPicUrls = [];

          newVal.forEach(function (v, i) {
            t_goodsPicUrls.push(consts.STATIC_URL+ '/goodsPic/' + v)
          });

          this.$set(this, 'goodsPicUrls', t_goodsPicUrls)
          this.$set(this.formValidate, 'pic', newVal.join(","))
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

  .demo-upload-list{
      display: inline-block;
      width: 60px;
      height: 60px;
      text-align: center;
      line-height: 60px;
      border: 1px solid transparent;
      border-radius: 4px;
      overflow: hidden;
      background: #fff;
      position: relative;
      box-shadow: 0 1px 1px rgba(0,0,0,.2);
      margin-right: 4px;
  }
  .demo-upload-list img{
      width: 100%;
      height: 100%;
  }
  .demo-upload-list-cover{
      display: none;
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
      background: rgba(0,0,0,.6);
  }
  .demo-upload-list:hover .demo-upload-list-cover{
      display: block;
  }
  .demo-upload-list-cover i{
      color: #fff;
      font-size: 20px;
      cursor: pointer;
      margin: 0 2px;
  }
</style>
