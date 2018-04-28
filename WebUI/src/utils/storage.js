const localStorage = window.localStorage

export default {
  name: 'storage',

  /**
   * 设置 localStorage
   * @param {string} key 键
   * @param {Object} value 值
   */
  set (key, value) {
    localStorage.setItem(key, JSON.stringify(value))
  },

  /**
   * 获取 localStorage
   * @param {string} key 键
   * @return {Object}
   */
  get (key) {
    try{
      return JSON.parse(localStorage.getItem(key)) || null
    }
    catch(err){
      return null;
    }
    
  },

  /**
   * 移除 localStorage
   * @param {string} key 键
   */
  remove (key) {
    localStorage.removeItem(key)
  }
}
