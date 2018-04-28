import types from './types'

export default { 
  [types.GET_All_DICTTYPES] (state, payload) {
    state.alldicttypes = payload.data
  },
  [types.GET_DICTTYPES] (state, payload) {
    state.dicttypes = payload.data
  },
  [types.GET_DICTTYPE] (state, payload) {
    state.dicttype = payload.data
  }
}
