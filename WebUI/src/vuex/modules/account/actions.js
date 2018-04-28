import types from './types'
import Model from '@/models/account'

export default {
  /**
   * 获取列表
   */
  getAccounts ({commit}, {params}) {
    return new Model().GET({params}).then((res) => {
      commit(types.GET_ACCOUNTS, {
        data: res.data
      })
    })
  },

  /**
   * 获取详情
   */
  getAccount ({commit}, {uri}) {
    return new Model().GET({uri}).then((res) => {
      commit(types.GET_ACCOUNT, {
        data: res.data
      })
    })
  },

  /**
   * 新增
   */
  postAccount ({commit}, {data}) {
    return new Model().POST({data})
  },

  /**
   * 编辑
   */
  putAccount ({commit}, {uri, data}) {
    return new Model().PUT({uri, data})
  },

  /**
   * 删除
   */
  deleteAccount ({commit}, {params}) {
    return new Model().DELETE({params})
  }
}
