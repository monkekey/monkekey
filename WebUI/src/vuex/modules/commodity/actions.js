import types from './types'
import Model from '@/models/commodity'

export default {
  /**
   * 获取列表
   */
  getAllCommoditys ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_All_DICTS, {
        data: res.data.data
      })
    })
  },
  /**
   * 获取列表
   */
  getCommoditys ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_DICTS, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getCommodity ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_DICT, {
        data: res.data
      })
    })
  },

  getCategoryCodeIn({ commit }, { uri }) {  
    return new Model().GET({ uri }).then((res) => {
      commit(types.GET_CATEGROYCODE, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postCommodity ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putCommodity ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteCommodity ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
