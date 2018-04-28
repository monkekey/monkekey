import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
  	allinns: [],
    inns: {'data':{'content':[],'totalElements':0}},
    inn: null
  },
  getters,
  actions,
  mutations
}
