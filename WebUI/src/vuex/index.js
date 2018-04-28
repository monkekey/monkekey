import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import actions from './actions'
import mutations from './mutations'
import chat from './modules/chat'
import inn from './modules/inn'
import dicttype from './modules/dicttype'
import dict from './modules/dict'
import goods from './modules/goods'
import order from './modules/order'
import room from './modules/room'
import account from './modules/account'
import service from './modules/service'
import reply from './modules/reply'
import commodity from './modules/commodity'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    language: 'zh-CN'
  },
  getters,
  actions,
  mutations,
  modules: {
    chat,
    inn,
    room,
    account,
    service,
    reply,
    dicttype,
    dict,
    goods,
    order,
    commodity
  }
})
