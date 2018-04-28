import types from './types'

export default {
  [types.GET_All_INNS] (state, payload) {
    state.allinns = payload.data
  },
  [types.GET_INNS] (state, payload) {
    state.inns = payload.data
  },
  [types.GET_INN] (state, payload) {
    state.inn = payload.data
  }
}
