import types from './types'

export default {
  [types.GET_ORDERS] (state, payload) {
    state.orders = payload.data
  },
  [types.GET_ORDER] (state, payload) {
    state.order = payload.data
  }
}
