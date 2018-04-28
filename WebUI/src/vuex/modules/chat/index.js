import getters from './getters'
import actions from './actions'
import mutations from './mutations'

export default {
  state: {
    //the user of current
    currentUser : {},
    //all users
    users : [],

    msgids : [],
    
    filterUser: '',

    currentSession : {'userName':''},

    currentCount : 0,

    online : false,

    suspended : false,

    broadcast : {},

    connection : null,

    notice : {
        show : false,
        type : '',
        msg : ''
    },
    
    topService : []
  },
  getters,
  actions,
  mutations
}
