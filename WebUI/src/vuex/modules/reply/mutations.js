import types from './types'

export default {
  [types.GET_REPLYS] (state, payload) {
    state.replys = payload.data
  },
  [types.GET_REPLY] (state, payload) {
    state.reply = payload.data
  }
}
