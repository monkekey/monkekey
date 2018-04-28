<style>
    .demo-spin-icon-load{
        animation: ani-demo-spin 1s linear infinite;
    }
    @keyframes ani-demo-spin {
        from { transform: rotate(0deg);}
        50%  { transform: rotate(180deg);}
        to   { transform: rotate(360deg);}
    }
    .demo-spin-col{
        height: 100px;
        position: relative;
        border: 1px solid #eee;
    }
</style>

<template>
  <div>
    <Modal
      width="300"
      v-model="del.modal"
      title="确认框"
      @on-ok="handleDelOk">
      <p>确认删除该记录？</p>
    </Modal>
    <Modal v-model="orderInfoShow" width="800">
        <p slot="header" style="text-align:center">
            <span>订单详情</span>
        </p>
        <div style="text-align:center">
            <Row v-show="LoadingVal">
              <Col class="demo-spin-col" span="8">
                  <Spin fix>
                      <Icon type="load-c" size=18 class="demo-spin-icon-load"></Icon>
                      <div>Loading</div>
                  </Spin>
              </Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">门店：{{orderInfo.hotelName}}</Col>
              <Col span="8">房间：{{orderInfo.orderRoom}}</Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">订单编号：{{orderInfo.orderNumber}}</Col>
              <Col span="8">支付编号：{{orderInfo.orderPaymentNumber}}</Col>
              <Col span="4">订单总额：{{orderInfo.orderSumPrice}}</Col>
              <Col span="4">订单状态：{{orderInfo.orderStatus === 0 ? '已支付' : (orderInfo.orderStatus === 1 ? '配送中' : (orderInfo.orderStatus === 2 ? '已送达' : '确认收货'))}}</Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">下单人：{{orderInfo.orderCreateby}}</Col>
              <Col span="8">下单时间：{{orderInfo.orderCreatetime}}</Col>
            </Row>
            <Row v-show="!LoadingVal" style="margin-bottom:15px;text-align: left;">
              <Col span="8">送货员：{{orderInfo.orderDeliveryby}}</Col>
              <Col span="8">送达时间：{{orderInfo.orderDeliverytime}}</Col>
            </Row>
            <Row v-show="!LoadingVal">
              <Table stripe :columns="orderDetail" :data="orderDaetailData"></Table>
            </Row>
        </div>
        <div slot="footer">
        </div>
    </Modal>


    <Breadcrumb style="display: flex; padding-bottom: 20px">
      <Breadcrumb-item href="/">首页</Breadcrumb-item>
      <Breadcrumb-item href="#">订单管理</Breadcrumb-item>
      <Breadcrumb-item>订单列表</Breadcrumb-item>
    </Breadcrumb>
    <List :current="current" :columns="columns" :data="order.orders.data.content"
      :total="order.orders.data.totalElements"
      @on-change="handlePageChange">
      <ListHeader>
        <ListOperations style="display: flex">
          <!-- <Button class="margin-right-sm" type="primary" @click="handleAdd">新增</Button> -->
        </ListOperations>
        <ListSearch>
          <Form ref="formInline" inline>
            <Form-item prop="search">
              <Select
                  v-model="search.innCode"
                  placeholder="请选择输入门店"
                  filterable
                  style="width: 230px;">
                  <Option v-for="item in inn.allinns" :value="item.code" :key="item.code">{{item.name}}</Option>
              </Select>
              <DatePicker type="date" placeholder="开始日期" v-model="search.beginDate" style="width: 120px"></DatePicker>
              <DatePicker type="date" placeholder="结束日期" v-model="search.endDate" style="width: 120px"></DatePicker>
              <Input type="text" placeholder="请输入关键字搜索" v-model="search.orderNo" style="width: 230px;"
                @on-enter="handleSearch"></Input>
            </Form-item>
            <Form-item>
              <Button type="primary" @click="handleSearch">查询</Button>
            </Form-item>
          </Form>
        </ListSearch>
      </ListHeader>
    </List>
  </div>
</template>

<script>
  import { mapState } from 'vuex'
  import consts from '@/utils/consts'
  import dateUtil from 'iview/src/utils/date'
  import List, { ListHeader, ListOperations, ListSearch } from '@/components/List'
  import order from '@/models/order'

  export default {
    name: 'list',
    components: {
      List,
      ListHeader,
      ListOperations,
      ListSearch
    },
    data () {
      return {
        del: {
          modal: false,
          id: 0
        },
        search: {
          beginDate: '',
          endDate: '',
          orderNo: '',
          innCode: ''
        },
        current: 1,
        columns: [
          {
            title: '所属门店',
            key: 'hotelName'
          },
          {
            title: '房间号',
            key: 'orderRoom'
          },
          {
            title: '订单号',
            key: 'orderNumber',
            width: 200
          },
          {
            title: '订单金额',
            key: 'orderSumPrice'
          },
          {
            title: '订单状态',
            key: 'orderStatus',
            render: (h, params) => {
                const row = params.row;
                let colorVal = row.orderStatus === 0 ? 'green' : (row.orderStatus === 1 ? 'orange' : (row.orderStatus === 2 ? 'orange' : 'blue')); 
                let textVal = row.orderStatus === 0 ? '已支付' : (row.orderStatus === 1 ? '配送中' : (row.orderStatus === 2 ? '已送达' : '确认收货')); 
                const color =colorVal
                const text = textVal

                return h('Tag', {
                    props: {
                        type: 'dot',
                        color: color
                    }
                }, text);
            }
          },
          {
            title: '操作',
            key: 'action',
            width: 100,
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.handleQuery(params.row.orderNumber)
                    }
                  }
                }, '查看详情')
              ])
            }
          }
        ],
        orderDetail:[
          {
              title: '类型',
              key: 'categoryName'
          },
          {
              title: '商品',
              key: 'commodityName'
          },
          {
              title: '数量',
              key: 'commodityAmount'
          },
          {
              title: '单价',
              key: 'commodityPrice'
          },
          {
              title: '总价',
              key: 'commoditySumPrice'
          }
          
        ],
        orderDaetailData:[],
        orderInfoShow:false,
        orderInfo:{},
        LoadingVal:true
      }
    },
    computed: mapState([
      'order',
      'inn'
    ]),
    created () {
      this.get()
    },
    methods: {
      get (current = 1) {
        this.$set(this, 'current', current)
        this.$store.dispatch('getOrders', {
          params: {
            pageIdx: (current - 1),
            pageSize: consts.PAGE_SIZE,
            ...this.search
          }
        })
      },
      handleAdd (query) {
          this.$router.push({ path: 'orders/form', query: { innCode: this.search.innCode}})
      },
      handlePageChange (current) {
        this.get(current)
      },
      handleSearch () {
        this.get()
        this.$set(this, 'current', 1)
      },
      handleEdit (id) {
        this.$router.push(`/orders/form/${id}`)
      },
      handleDel (id) {
        this.$set(this.del, 'modal', true)
        this.$set(this.del, 'id', id)
      },
      handleDelOk () {
        this.$store.dispatch('deleteOrder', {
          params: {
            orderNo: this.del.id
          }
        }).then(() => {
          this.$Message.success('删除成功！')
          this.get()
        })
      },
      handleQuery(val){
        this.orderInfoShow = true
        new order().addPath(val).GET().then((res) =>{
          if(res.data.success == true){
            let info = res.data.data
            this.orderInfo = info
            this.orderDaetailData = info.sOrderDetails
            this.LoadingVal = false
          }else{
            this.$Message.error('查看详情失败，'+res.data.data)
            this.orderInfoShow = false
          }
        })
      }
    },
    watch:{
      'search.beginDate': {
          handler (newVal) {
              this.$set(this.search, 'beginDate', dateUtil.format(newVal, 'yyyy-MM-dd'))
          }
      },
      
      'search.endDate': {
          handler (newVal) {
              this.$set(this.search, 'endDate', dateUtil.format(newVal, 'yyyy-MM-dd'))
          }
      }
      //dateUtil.format(this.time, 'yyyy-MM-dd'),
    }
  }
</script>
