//额外的公共函数
const extraApp = {
  setWxStorageSync: (key, data) => {
    try {
      wx.setStorageSync(key, data);
    } catch (e) {
      console.erroe(`添加${key}错误`)
      return null;
    }
  },
  getWxStorageSync: (key) => {
    try {
      return wx.getStorageSync(key);
    } catch (e) {
      console.erroe(`获取${key}错误`)
      return null;
    }
  },
  removeWxStorageSync: (key) => {
    try {
      wx.removeStorageSync(key);
    } catch (e) {
      console.erroe(`删除${key}错误`)
      return null;
    }
  },
  clearWxStorageSync: () => {
    try {
      wx.clearStorageSync()
    } catch (e) {
      console.erroe(`clearWxStorageSync错误`)
      return null;
    }
  }
}
module.exports = extraApp;