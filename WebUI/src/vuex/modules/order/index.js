import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
    orders: {'data':{'content':[],'totalElements':0}},
    order: null
  },
  getters,
  actions,
  mutations
}
