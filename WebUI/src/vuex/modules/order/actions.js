import types from './types'
import Model from '@/models/order'

export default {
  
  /**
   * 获取列表
   */
  getOrders ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_ORDERS, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getOrder ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_ORDER, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postOrder ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putOrder ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteOrder ({commit}, {params}) {
    debugger
    return new Model().DELETE({params})
  }
}
