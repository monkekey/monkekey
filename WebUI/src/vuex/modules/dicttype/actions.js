import types from './types'
import Model from '@/models/dicttype'

export default { 
  /**
   * 获取列表
   */
  getAllDictTypes ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_All_DICTTYPES, {
        data: res.data.data
      })
    })
  },
  /**
   * 获取列表
   */
  getDictTypes ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_DICTTYPES, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getDictType ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_DICTTYPE, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postDictType ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putDictType ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteDictType ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
