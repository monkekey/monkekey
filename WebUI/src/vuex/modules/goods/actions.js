import types from './types'
import Model from '@/models/goods'

export default {
  /**
   * 获取列表
   */
  getGoodses ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_GOODSES, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getGoods ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_GOODS, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postGoods ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putGoods ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteGoods ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
