const extraApp = require('extraApp.js');
const lavandeAPI = require('api/LavandeAPI.js')

const app = {
  onLaunch: function () {
    this.getUserInfo();
    this.getSystemInfo();
  },

  getUserInfo: function (cb) {
    var that = this;
    //调用登录接口
    wx.login({
      success: function (res) {
        let code = res.code;
        wx.getUserInfo({
          success: function (userRes) {
            that.globalData.userInfo = userRes.userInfo
            getApp().setWxStorageSync('LavandeUserInfo', userRes.userInfo);
            lavandeAPI.getOpenId({ platformCode: that.globalData.platformCode, code: code }, function (openIdRes) {
              if (openIdRes.success) {
                let openid = openIdRes.data.openid;
                that.globalData.openid = openid;
                getApp().setWxStorageSync('LavandeOpenid', openid);

                typeof cb == "function" && cb(openid)
              }
            })
          },
          fail: function () {
            
          }
        })
      }, fail: function () {
       
      }
    })
  },
  getSystemInfo: function () {
    var that = this;
    if (!this.globalData.SystemInfo.windowWidth) {
      wx.getSystemInfo({
        success: function (res) {
          that.globalData.SystemInfo = res
        }
      })
    }
  },
  globalData:{
    platformCode: 'Hanpudun', //应用CODE
    userInfo: {},
    openid: '',
    wxUserInfo: {},
    currentInnCode: '',
    currentRoomNo: '',
    SystemInfo: {},
    payMethodList: [{
      name: '到店支付',
      code: 'face',
      class: ''
    }, {
      name: '微信支付',
      code: 'wx',
      class: ''
    }],
    imgUrl: 'http://yumi-icon.oss-cn-shenzhen.aliyuncs.com/wxyumibnb/',
    imghotel: 'http://iyumi-web.oss-cn-shenzhen.aliyuncs.com/',
    payNotifyUrl: 'http://api.iyumi.com/web/booking_order/pay/WXCallBack',
    imgAccuracy: '',
    location: {
      latitude: '',
      longitude: '',
    }
  }
};

App(Object.assign(app, {}, extraApp));