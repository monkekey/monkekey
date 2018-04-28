import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
    rooms: {'data':{'content':[],'totalElements':0}},
    room: null
  },
  getters,
  actions,
  mutations
}
