import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
    goodses: {'data':{'content':[],'totalElements':0}},
    goods: null
  },
  getters,
  actions,
  mutations
}
