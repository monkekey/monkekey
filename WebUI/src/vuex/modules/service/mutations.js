import types from './types'

export default {
  [types.GET_SERVICES] (state, payload) {
    state.services = payload.data
  },
  [types.GET_SERVICE] (state, payload) {
    state.service = payload.data
  }
}
