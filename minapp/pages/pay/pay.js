var WechatDataAPI = require('../../api/WechatDataAPI.js');
var lavandeAPI = require('../../api/LavandeAPI.js');
const Request = require('../../utils/RequestUtil.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    textValue:'',
    price:0.01,
    userName:'',
    scenes:'',
    web_base_url: Request.BASE_URL,
    scens:[],
    imgUrl:'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/xiaoer.png'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let scene = decodeURIComponent(options.scenes).split("_")
    this.setData({
      price: options.price,
      userName: options.username,
      scenes: options.scenes,
      scene: scene,
      imgUrl: options.imgUrl ? options.imgUrl + "?" + Math.random() / 9999 : 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/xiaoer.png'
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 多行文本框绑定数据事件
   */
  bindKeyInput: function (e) {
    this.setData({
      textValue: e.detail.value
    })
  },

  /**
   * 微信支付
   */
  payOrder: function () {
    var order = 'Bee-zanshang' + new Date().getTime();
    var _this = this;
    var openid = wx.getStorageSync('LavandeOpenid')
    wx.showLoading({
      title: '加载中',
    })
    if (!openid) {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '支付失败，获取不到用户信息，请重新赞赏',
        showCancel: false,
        success: function (sres) {
          wx.switchTab({
            url: '../pages/pay/pay?price =' + _this.data.price + " & username=" + _this.data.userName
          })
        }
      })
      return;
    }
    
    WechatDataAPI.actPayData({
      // platformCode: getApp().globalData.platformCode,
      platformCode: 'WECHAT_XCX_BEE',
      outTradeNo: order,
      amount: _this.data.price,
      body: '赞赏'+ _this.data.userName + ":" + _this.data.price + "元",
      notifyUrl: getApp().globalData.payNotifyUrl,
      attach: 'BEE-ZANGSHANG' + new Date().getTime(),
      openId: openid,
    }, function (payDataRes) {
      wx.hideLoading()
      if (!payDataRes.success) {
        wx.showModal({
          title: '提示',
          content: payDataRes.errMsg,
          showCancel: false,
          success: function (showModalRes) { }
        })
        return;
      }
      var payData = payDataRes.result;
      wx.requestPayment({
        'timeStamp': payData.timeStamp,
        'nonceStr': payData.nonceStr,
        'package': payData.package,
        'signType': payData.signType,
        'paySign': payData.paySign,
        'success': function (pres) {
          wx.showModal({
            title: '提示',
            content: '支付成功',
            showCancel: false,
            success: function (res) {
              let scene = _this.data.scene
              let admireInfo = {
                "innCode": scene[0],
                "name": _this.data.username,
                "price": _this.data.price,
                "remark": _this.data.textValue,
                "order": order
              }
              lavandeAPI.postAdmireInfo(JSON.stringify(admireInfo), function (respon) {

              })
              // wx.navigateTo({
              //   url: "/pages/index/index?scenes=" + _this.data.scenes
              // })
            }
          })
        },
        'fail':function(fres){
          console.log(fres)
        }
      })
    });
  }
})