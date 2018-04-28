import types from './types'

export default {
  [types.GET_GOODSES] (state, payload) {
    state.goodses = payload.data
  },
  [types.GET_GOODS] (state, payload) {
    state.goods = payload.data
  }
}
