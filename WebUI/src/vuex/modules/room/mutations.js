import types from './types'

export default {
  [types.GET_ROOMS] (state, payload) {
    state.rooms = payload.data
  },
  [types.GET_ROOM] (state, payload) {
    state.room = payload.data
  }
}
