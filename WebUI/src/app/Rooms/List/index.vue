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
      <Breadcrumb-item href="/inns">分店管理</Breadcrumb-item>
      <Breadcrumb-item href="/inns">分店列表</Breadcrumb-item>
      <Breadcrumb-item>分店房间</Breadcrumb-item>
    </Breadcrumb>
    <List :current="current" :columns="columns" :data="room.rooms.data.content"
      :total="room.rooms.data.totalElements"
      @on-change="handlePageChange">
      <ListHeader>
        <ListOperations style="display: flex">
          <Button class="margin-right-sm" type="primary" @click="handleAdd">新增</Button>
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
              <Input type="text" placeholder="请输入房间号" v-model="search.roomNo" style="width: 230px;"
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
          innCode: '',
          roomNo: ''
        },
        current: 1,
        columns: [
          {
            title: 'ID',
            key: 'id',
            width: 80
          },
          {
            title: '酒店编码',
            key: 'innCode',
          },
          {
            title: '房间号',
            key: 'roomNo',
            ellipsis: true
          },
          {
            title: '最后操作人',
            key: 'lastModifyBy',
            width: 140
          },
          {
            title: '操作时间',
            key: 'lastModifyTime',
            width: 160,
            ellipsis: true
          },
          {
            title: '操作',
            key: 'action',
            width: 160,
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
                      this.handleQrcode(params.row.id)
                    }
                  }
                }, '二维码'),
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
      'room',
      'inn'
    ]),
    created () {
      this.search.innCode = this.$route.query.innCode || ''
      this.get()
    },
    methods: {
      get (current = 1) {
        this.$set(this, 'current', current)

        this.$store.dispatch('getRooms', {
          params: {
            pageIdx: (current - 1),
            pageSize: consts.PAGE_SIZE,
            ...this.search
          }
        })
      },
      handleAdd (query) {
          this.$router.push({ path: 'rooms/form', query: { innCode: this.search.innCode}})
      },
      handlePageChange (current) {
        this.get(current)
      },
      handleSearch () {
        this.get()
        this.$set(this, 'current', 1)
      },
      handleEdit (id) {
        this.$router.push(`/rooms/form/${id}`)
      },
      handleQrcode (id){
        let url = consts.API_URL + '/api/wx/getQrcode?id=' + id
        window.open(url)
      },
      handleDel (id) {
        this.$set(this.del, 'modal', true)
        this.$set(this.del, 'id', id)
      },
      handleDelOk () {
        this.$store.dispatch('deleteRoom', {
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
