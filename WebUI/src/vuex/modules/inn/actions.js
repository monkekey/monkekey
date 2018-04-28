import types from './types'
import Model from '@/models/inn'

export default { /**
   * 获取列表
   */
  getAllInns ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_All_INNS, {
        data: res.data.data
      })
    })
  },
  /**
   * 获取列表
   */
  getInns ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_INNS, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getInn ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_INN, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postInn ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putInn ({commit}, {uri, data}) {
    debugger;
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteInn ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
