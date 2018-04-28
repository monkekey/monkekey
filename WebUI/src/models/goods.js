import REST from '@/utils/rest'
import consts from '@/utils/consts'
import restHelpers from '@/utils/restHelpers'

export default class extends REST {
  constructor () {
    super()
    let token = JSON.parse(localStorage.getItem('lavande_butler_token'));
    this.baseURL = consts.API_URL
    this.errorHandler = restHelpers.errorHandler
    this.path = 'sys/goods'
    this.addHeaders({"Authorization": "Bearer " + token})
  }
}
