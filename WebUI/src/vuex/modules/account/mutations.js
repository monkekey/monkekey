import types from './types'

export default {
  [types.GET_ACCOUNTS] (state, payload) {
    state.accounts = payload.data
  },
  [types.GET_ACCOUNT] (state, payload) {
    state.account = payload.data
  }
}
