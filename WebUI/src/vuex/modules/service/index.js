import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
    services: {'data':{'content':[],'totalElements':0}},
    service: null
  },
  getters,
  actions,
  mutations
}
