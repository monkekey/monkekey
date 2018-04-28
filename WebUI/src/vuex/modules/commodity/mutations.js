import types from './types'

export default {
  [types.GET_All_DICTS] (state, payload) {
    state.alldicts = payload.data
  },
  [types.GET_DICTS] (state, payload) {
    state.dicts = payload.data
  },
  [types.GET_DICT] (state, payload) {
    state.dict = payload.data
  },
  [types.GET_CATEGROYCODE](state, payload) {
    state.categoryCodeIn = payload.data
  }
}
