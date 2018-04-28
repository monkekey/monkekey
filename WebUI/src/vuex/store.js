import Vue from 'vue'
import Vuex from 'vuex'

import getters from './getters'
import actions from './actions'
import consts from '@/utils/consts'


Vue.use(Vuex)

//create a pbject to save the state of app at start
const state = {
    //the user of current
    currentUser : {},
    //all users
    users : [],
    filterUser: '',

    currentSession : {'userName':''},

    currentCount : 0,

    online : false,

    broadcast : {},

    connection : null,

    notice : {
        show : false,
        type : '',
        msg : ''
    }
}

//create a object to save the function of mutation
const mutations = {
}

export default new Vuex.Store({
    state,
    getters,
    actions,
    mutations
});