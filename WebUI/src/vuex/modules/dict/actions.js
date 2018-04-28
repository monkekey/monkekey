import types from './types'
import Model from '@/models/dict'

export default {
  /**
   * 获取列表
   */
  getAllDicts ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_All_DICTS, {
        data: res.data.data
      })
    })
  },
  /**
   * 获取列表
   */
  getDicts ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_DICTS, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getDict ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_DICT, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postDict ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putDict ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteDict ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
