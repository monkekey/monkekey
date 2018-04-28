<template>
  <div>
    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">门店门店商品管理</Breadcrumb-item>
      <Breadcrumb-item>门店商品列表</Breadcrumb-item>
    </Breadcrumb>

    <Row type="flex">
      <Col span=20>
        <Row>
          <Col span="11">
          门店：
            <Select
                  v-model="hotelCode"
                  placeholder="请选择查询门店"
                  filterable
                  style="width: 230px;"
                  label-in-value
                  @on-change="changeHotel">
                  <Option v-for="item in HotelList" :value="item.code" :key="item.code">{{item.name}}</Option>
            </Select>
          </Col>
          <Col span="11">
          商品名称：
            <Input type="text" placeholder="请输入关键字搜索" v-model="keyword" style="width: 230px;"
                @on-enter="handleSearch"></Input><Button style="margin-left:5px" type="primary" @click="handleSearch">查询</Button>
          </Col>
        </Row>
      </Col>
      <Col span=4>
        <Button class="margin-right-sm" type="primary" @click="handleAdd">新增</Button>
      </Col>  
    </Row>
    <Row type="flex" justify="space-between" style="margin-top: 20px;padding-top: 10px;border-top: 1px solid #e8e8e8;" v-show="hotelShow">
      <Col style="font-size: large;font-weight: bold;">
        门店：{{hotelName}}
      </Col>
       
    </Row>  
    <Row style="margin-top:10px">
      <Col>
        <Table :loading="loadingDate" border :columns="CommodityColumns" :data="CommodityData"></Table>
      </Col>
    </Row>
    <Row style="margin-top:10px">
      <Col>
        <Page :total="pageCount" :current="initPage" @on-change="changePage"></Page>
      </Col>
    </Row>
  
  </div>
</template>

<script>
  import { mapState } from 'vuex'
  import consts from '@/utils/consts'
  import List, { ListHeader, ListOperations, ListSearch } from '@/components/List'
  import Hotel from '@/models/inn'
  import HotelCommodity from '@/models/hotelCommodity'
  export default {
    name: 'list',
    // components: {
    //   List,
    //   ListHeader,
    //   ListOperations,
    //   ListSearch
    // },
    data () {
      return {
        del: {
          modal: false,
          id: 0
        },
        search: {
          category: '',
          goodsName: ''
        },
        current: 1,
        HotelList:[],
        CommodityData:[],
        CommodityColumns: [
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
            title: '库存',
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
                        console.log(event)
                        this.CommodityData[params.index].commodityInventory = event
                        this.$emit('InputNumber',event) 
                      }
                    }
                })
              ])
            }
          },
          {
            title: '操作',
            key: 'action',
            width: 125,
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'info',
                    size: 'small'
                  },
                  style:{
                    'margin-right': '5px'
                  },
                  on: {
                    click: () => {
                      this.save(params.row)
                    }
                  }
                }, '保存'),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.delCommotidy(params.row.id)
                    }
                  }
                }, '删除')
              ])
            }
          }
        ],
        hotelCode:"",
        keyword:"",
        pageCount:0,
        pageDate:{
          pageIdx:0,
          pageSize:10
        },
        hotelShow:false,
        hotelName:'',
        loadingDate:false,
        initPage:1,
      }
    },
    // computed: {
    //   ...mapState([
    //     'goods',
    //     'dict'
    //   ]),
    //   filterGoodsType: function () {
    //     var allDicts = this.dict.alldicts;
    //     return allDicts.filter( item => item.typeCode == 'GOODSTYPE')
    //   }
    // },
    // created () {
    //   this.get()
    // },
    methods: {
      // get (current = 1) {
      //   this.$set(this, 'current', current)
      //   this.$store.dispatch('getGoodses', {
      //     params: {
      //       pageIdx: (current - 1),
      //       pageSize: consts.PAGE_SIZE,
      //       ...this.search
      //     }
      //   })

      //   if (this.dict.alldicts.length == 0) {
      //     let uri = 'all';
      //     this.$store.dispatch('getAllDicts', {uri}) 
      //   }
      // },
      init(){
        new Hotel().addPath('all').GET().then((res) =>{
          console.log(res)
          if(res.data.success == true){
            this.HotelList = res.data.data
          }else{
            this.$Message.error('获取门店列表失败！')
          }
        })
      },
      handleAdd (query) {
          this.$router.push({ path: 'goodses/form', query: { innCode: this.search.innCode}})
      },
      handlePageChange (current) {
        this.get(current)
      },
      handleSearch () {
        this.pageCount=0
        this.pageDate={
          pageIdx:0,
          pageSize:10
        }
        this.queryCommodity()
        // this.get()
        // this.$set(this, 'current', 1)
      },
      queryCommodity(){
        this.loadingDate = true
        let hotelID = this.hotelCode
        let keyword = this.keyword
        let pageIdx = this.pageDate.pageIdx
        let pageSize = this.pageDate.pageSize
        let params = {
          hotelID:hotelID,
          keyword:keyword,
          pageIdx:pageIdx,
          pageSize:pageSize
        }
        new HotelCommodity().addPath('queryHotelAndCommodityName').GET({params}).then((res) =>{
          console.log(res)
          if(res.data.success == true){
            this.CommodityData = res.data.data
            this.pageCount = res.data.data[0].totalCount
            console.log(this.pageCount)
            this.hotelShow = true
            this.loadingDate = false
          }else{
            this.$Message.error('获取门店商品列表失败！')
            this.loadingDate = false
          }
        })
      },
      changeHotel(e){
        this.hotelName = e.label
      },
      save(val){
        console.log(val)
        let data = {
          id:val.id,
          commodityInventory: val.commodityInventory
        }
        data = JSON.stringify(data)
        new HotelCommodity().addPath('update').PUT({data}).then((res) =>{
          console.log(res)
          if(res.data.data == "ok"){
            this.$Message.info("修改成功")
            this.queryCommodity()
          }else{
            this.queryCommodity()
            this.$Message.error("修改失败," + res.data.data )
          }
        })
      },
      delCommotidy(val){
        this.$Modal.confirm({
            title:'删除门店商品？',
            content:'确定要删除该商品么？此操作将不能撤回',
            onOk:()=>{
              
              new HotelCommodity().addPath('del?id=' + val).DELETE().then((res) =>{
                console.log(res)
                if(res.data.data == "ok"){
                  this.$Message.info("修改成功")
                  this.queryCommodity()
                }else{
                  this.queryCommodity()
                  this.$Message.error("修改失败," + res.data.data)
                }
              })
            }
        })
      },
      changePage(e){//翻页
        let _this = this
        _this.initPage = e;
        _this.pageDate.pageIdx = (e-1)*10
        _this.queryCommodity()
      },
    },
    mounted(){
      this.init()
    },
    watch: {
      'goods.goodses': {
        handler (newVal) {
          var allGoodsType = this.dict.alldicts.filter( item => item.typeCode == 'GOODSTYPE')
          newVal.data.content.foreach(function (v, i) {
            allGoodsType.foreach(function (v2, i2) {
              if (v2.itemCode = v.category) {
                v.categoryName = v2.itemname
              }
            })
          });
        }
      }
    }
  }
</script>
