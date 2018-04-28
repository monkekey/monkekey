import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
  	alldicts: [],
    dicts: {'data':{'content':[],'totalElements':0}},
    dict: null,
    categoryCodeIn:[]
  },
  getters,
  actions,
  mutations
}
