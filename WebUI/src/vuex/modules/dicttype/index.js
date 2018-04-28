import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
  	alldicttypes: [],
    dicttypes: {'data':{'content':[],'totalElements':0}},
    dicttype: null
  },
  getters,
  actions,
  mutations
}
