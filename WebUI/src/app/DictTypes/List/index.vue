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
      <Breadcrumb-item href="#">商品类型管理</Breadcrumb-item>
      <Breadcrumb-item>商品类型列表</Breadcrumb-item>
    </Breadcrumb>
    <List :current="current" :columns="columns" :data="dicttype.dicttypes.data.content"
      :total="dicttype.dicttypes.data.totalElements"
      @on-change="handlePageChange">
      <ListHeader>
        <ListOperations style="display: flex">
          <Button class="margin-right-sm" type="primary" @click="handleAdd">新增</Button>
        </ListOperations>
        <ListSearch>
          <Form ref="formInline" inline>
            <Form-item prop="search">
              <Input type="text" placeholder="请输入关键字搜索" v-model="search.keywords" style="width: 230px;"
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
          keywords: ''
        },
        current: 1,
        columns: [
          {
            title: 'ID',
            key: 'id',
            width: 80
          },
          {
            title: '类型编码',
            key: 'categoryCode'
          },
          {
            title: '类型名称',
            key: 'categoryName',
          },
          {
            title: '是否系统内置',
            key: 'categoryIsFixed',
            render: (h, params) => {
                return h('div', [
                    h('strong', params.row.categoryIsFixed==1?"是":"否")
                ]);
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
      'dicttype'
    ]),
    created () {
      this.get()
    },
    methods: {
      get (current = 1) {
        this.$set(this, 'current', current)
        this.$store.dispatch('getDictTypes', {
          params: {
            pageIdx: (current - 1),
            pageSize: consts.PAGE_SIZE,
            ...this.search
          }
        })
      },
      handleAdd (query) {
          this.$router.push({ path: 'dicttypes/form', query: { innCode: this.search.innCode}})
      },
      handlePageChange (current) {
        this.get(current)
      },
      handleSearch () {
        this.get()
        this.$set(this, 'current', 1)
      },
      handleEdit (id) {
        this.$router.push(`/dicttypes/form/${id}`)
      },
      handleDel (id) {
        this.$set(this.del, 'modal', true)
        this.$set(this.del, 'id', id)
      },
      handleDelOk () {
        this.$store.dispatch('deleteDictType', {
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
