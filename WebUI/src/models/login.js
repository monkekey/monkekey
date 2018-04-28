import REST from '@/utils/rest'
import consts from '@/utils/consts'
import restHelpers from '@/utils/restHelpers'

export default class extends REST {
  constructor () {
    super()
    this.baseURL = consts.API_URL
    this.errorHandler = restHelpers.errorHandler
    this.path = 'auth'
  }
}
