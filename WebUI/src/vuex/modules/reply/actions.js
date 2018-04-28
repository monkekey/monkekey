import types from './types'
import Model from '@/models/reply'

export default {
  /**
   * 获取列表
   */
  getReplys ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_REPLYS, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getReply ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_REPLY, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postReply ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putReply ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteReply ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
