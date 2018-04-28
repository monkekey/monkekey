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
      <Breadcrumb-item href="#">分店管理</Breadcrumb-item>
      <Breadcrumb-item>分店列表</Breadcrumb-item>
    </Breadcrumb>
    <List :current="current" :columns="columns" :data="inn.inns.data.content"
      :total="inn.inns.data.totalElements"
      @on-change="handlePageChange">
      <ListHeader>
        <ListOperations style="display: flex">
          <Button class="margin-right-sm" type="primary" @click="$router.push('inns/form')">新增</Button>
        </ListOperations>
        <ListSearch>
          <Form ref="formInline" inline>
            <Form-item prop="innName">
              <Input type="text" placeholder="请输入酒店名称" v-model="search.innName" style="width: 230px;"
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
          innName: ''
        },
        current: 1,
        columns: [
          {
            title: 'ID',
            key: 'id',
            width: 60,
            fixed: 'left'
          },
          {
            title: '酒店编码',
            key: 'innCode',
            width: 120,
            fixed: 'left'
          },
          {
            title: '酒店名称',
            key: 'innName',
            fixed: 'left'
          },
          {
            title: '经度',
            key: 'latitude',
            width: 100,
            ellipsis: true
          },
          {
            title: '纬度',
            key: 'longitude',
            width: 100,
            ellipsis: true
          },
          {
            title: '最后操作人',
            key: 'lastModifyBy',
            width: 100
          },
          {
            title: '操作时间',
            key: 'lastModifyTime',
            ellipsis: true
          },
          {
            title: '操作',
            key: 'action',
            width: 210,
            fixed: 'right',
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
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.handleDel(params.row.id)
                    }
                  }
                }, '删除'),
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
                      this.handleRoom(params.row.innCode)
                    }
                  }
                }, '房间'),
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
                      this.handleButler(params.row.innCode)
                    }
                  }
                }, '管家')
              ])
            }
          }
        ]
      }
    },
    computed: mapState([
      'inn',
      'dicttype'
    ]),
    created () {
      this.get()
    },
    methods: {
      get (current = 1) {
        this.$set(this, 'current', current)

        if (current == 1) {
          let uri = 'all';
          this.$store.dispatch('getAllInns', {uri}) 
          this.$store.dispatch('getAllDictTypes', {uri}) 
        }

        this.$store.dispatch('getInns', {
          params: {
            pageIdx: (current - 1),
            pageSize: consts.PAGE_SIZE,
            ...this.search
          }
        })
      },
      handlePageChange (current) {
        this.get(current)
      },
      handleSearch () {
        this.get()
        this.$set(this, 'current', 1)
      },
      handleRoom (innCode) {
        this.$router.push({ path: '/rooms', query: { innCode: innCode }})
      },
      handleButler (innCode) {
        this.$router.push({ path: '/accounts', query: { innCode: innCode }})
      },
      handleEdit (id) {
        this.$router.push(`/inns/form/${id}`)
      },
      handleDel (id) {
        this.$set(this.del, 'modal', true)
        this.$set(this.del, 'id', id)
      },
      handleDelOk () {
        this.$store.dispatch('deleteInn', {
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
