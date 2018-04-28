import Model from '@/models/chat'

export default {
    searchUser: ({ commit }, filterUser) => {
        commit('FILTER_USER', filterUser);
    },

    selectSession : ({ commit }, userId) => {
        commit('CHANGE_SESSION', userId);
    },

    setUser : ({ commit }, user) => {
        if (user.userId && user.account && user.userName) {
            commit('SET_USER', user);
        }
    },

    addUser : ({ commit }, user) => {
        //&& user.userHead
        if (user instanceof Array || user.userId && user.userName) {
            commit('ADD_USER', user);
        }
    },

    removeUser : ({ commit }, userId) => {
        commit('REMOVE_USER', userId);
    },

    setConn : ({ commit }, conn) => {
        commit('SET_CONN', conn);
    },

    changeStatus : ({ commit }, status) => {
        commit('CHANGE_STATUS', status);
    },

    changeSuspended : ({ commit }, status) => {
        commit('CHANGE_SUSPENDED', status);
    },

    addMessage : ({ commit }, message) => {
        if (message.is_self != 1) {
            commit('SET_HAS_MESSAGE',{userId:message.from, status:true});
        }
        
        commit('ADD_MESSAGE',message);
    },

    updateMessage : ({ commit }, payload) => {
        commit('UPDATE_MESSAGE',payload);
    },

    setHasMessageStatus : ({ commit }, payload) => {
        commit('SET_HAS_MESSAGE', payload);
    },

    setCount : ({ commit }, count) => {
        commit('SET_COUNT', count);
    },

    showNotice : ({ commit }, msg, type) => {
        commit('SHOW_NOTICE', msg, type);
    },

    /**
   * 获取列表
   */
    getTopService ({commit}, {params}) {
        return new Model().addPath("getTopService").GET({params}).then((res) => {
            commit("TOP_SERVICE", {
                data: res.data.data.content
            })
        })
    },
};
