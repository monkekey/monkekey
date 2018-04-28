import types from './types'
import Model from '@/models/room'

export default {
  /**
   * 获取列表
   */
  getRooms ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_ROOMS, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getRoom ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_ROOM, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postRoom ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putRoom ({commit}, {uri, data}) {
    debugger;
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteRoom ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
