import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
    accounts: {'data':{'content':[],'totalElements':0}},
    account: null
  },
  getters,
  actions,
  mutations
}
