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
      <Breadcrumb-item href="#">商品</Breadcrumb-item>
      <Breadcrumb-item>商品列表</Breadcrumb-item>
    </Breadcrumb>
    <List :current="current" :columns="columns" :data="dict.dicts.data.content"
      :total="dict.dicts.data.totalElements"
      @on-change="handlePageChange">
      <ListHeader>
        <ListOperations style="display: flex">
          <Button class="margin-right-sm" type="primary" @click="handleAdd">新增</Button>
        </ListOperations>
        <ListSearch>
          <Form ref="formInline" inline>
            <Form-item prop="search">
              <Select
                  v-model="search.typeCode"
                  placeholder="请选择字典类型"
                  filterable
                  style="width: 230px;">
                  <Option value="">所有类型</Option>
                  <Option v-for="item in dicttype.alldicttypes" :value="item.categoryCode" :key="item.categoryCode">{{item.categoryName}}</Option>
              </Select>
              <Input type="text" placeholder="请输入关键字搜索" v-model="search.itemName" style="width: 230px;"
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
  import List, { ListHeader, ListOperations, ListSearch } from '@/components/List'

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
          typeCode: '',
          itemName: ''
        },
        current: 1,
        columns: [
          {
            title: 'ID',
            key: 'id',
            width: 80
          },
          {
            title: '商品类型',
            key: 'categoryName'
          },
          {
            title: '商品编码',
            key: 'commodityCode'
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
            key: 'commoditySynopsis',
          },
          // {
          //   title: '商品库存',
          //   key: 'commodityInventory',
          // },
          {
            title: '商品售价',
            key: 'commodityPrice',
          },
          {
            title: '商品进货价',
            key: 'commodityCost',
          },
          {
            title: '操作',
            key: 'action',
            width: 125,
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
                      this.handleEdit(params.row.id)
                    }
                  }
                }, '编辑'),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.handleDel(params.row.id)
                    }
                  }
                }, '删除')
              ])
            }
          }
        ]
      }
    },
    computed: mapState([
      'dict',
      'dicttype'
    ]),
    created () {
      this.get()
    },
    methods: {
      
      get (current = 1) {
        console.log(this.dicttype.alldicttypes)
        this.$set(this, 'current', current)
        this.$store.dispatch('getCommoditys', {
          params: {
            pageIdx: (current - 1),
            pageSize: consts.PAGE_SIZE,
            ...this.search
          }
        })
        if (this.dicttype.alldicttypes.length == 0) {
          let uri = 'all';
          this.$store.dispatch('getAllDictTypes', {uri}) 
        }
      },
      handleAdd (query) {
          this.$router.push({ path: 'dicts/form', query: { innCode: this.search.innCode}})
      },
      handlePageChange (current) {
        this.get(current)
      },
      handleSearch () {
        this.get()
        this.$set(this, 'current', 1)
      },
      handleEdit (id) {
        this.$router.push(`/dicts/form/${id}`)
      },
      handleDel (id) {
        this.$set(this.del, 'modal', true)
        this.$set(this.del, 'id', id)
      },
      handleDelOk () {
        this.$store.dispatch('deleteCommodity', {
          params: {
            id: this.del.id
          }
        }).then(() => {
          this.$Message.success('删除成功！')
          this.get()
        })
      }
    }
  }
</script>
