// pages/roomProp/roomProp.js
const app = getApp();
let roomProp = {
  data: {
    globalData: app.globalData,
  },

  onLoad: function (options) {
    let proplist = JSON.parse(options.roomProp);
    console.log(proplist)
    this.setData({ proplist})
  },

  onShow: function () {

  },
}
Page(Object.assign(roomProp,{}))