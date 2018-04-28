import types from './types'
import Model from '@/models/service'

export default {
  /**
   * 获取列表
   */
  getServices ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_SERVICES, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getService ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_SERVICE, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postService ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putService ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteService ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
