// pages/orderPay/orderPay.js
const app = getApp();
var WechatDataAPI = require('../../api/WechatDataAPI.js');
const platformAPI = require('../../api/PlatformAPI.js')
const Moment = require('../../utils/Moment.js')
const orderPayData = {
  data: {
    globalData: app.globalData,
    userInfo: app.globalData.userInfo,
    roomInfo: {},
  },

  onLoad: function (options) {
    let userInfo = wx.getStorageSync('LavandeUserInfo');
    let roomInfo = wx.getStorageSync('roomInfo');
    let orderInfo = wx.getStorageSync('orderInfo');
    var orderPrice = wx.getStorageSync('orderPrice');
    let beginTime = orderPrice.checkInDate;
    let endTime = orderPrice.checkOutDate;
    var dayPrice = orderPrice.dayPrice;
    let cheakInDay = Moment(endTime.replace(/\-/g, '/')).differ(beginTime.replace(/\-/g, '/'))
    beginTime = beginTime.replace(/\//g, '-')
    endTime = endTime.replace(/\//g, '-')
    let payList = [];
    let amount = 0;
      for (var i = 0; i < dayPrice.length; i++) {
        for (var key in dayPrice[i]) {
          amount += parseFloat(dayPrice[i][key]);
          payList.push({
            accDate: key,
            price: dayPrice[i][key],
            priceType: 'roomRate'
          });
        }
      }
    // payList.push({
    //   accDate: '押金',
    //   price: 188,
    //   priceType: 'deposit'
    // })
    // amount += 188;
      this.setData({ userInfo, roomInfo, orderInfo, beginTime, endTime, cheakInDay, payList, amount})
      this.getOrderDatiel()
  },

  onShow: function () {

  },
  getOrderDatiel: function () {//入住人信息
    let that = this;
    let orderNo = this.data.orderInfo.orderNo;
    wx.showLoading({})
    platformAPI.getOrderDetail({ orderNo: orderNo }, function (res) {
      wx.hideLoading();
      if (res.success) {
        let guestlist = res.data.orderGuestList;
        that.setData({ guestlist })
      } else {
        wx.showModal({
          title: '提示',
          content: res.errMsg ? res.errMsg : res,
        })
        return;
      }
    })
  },

  onBill(){
    console.log('立即支付')
    var order = 'Bee-zanshang' + new Date().getTime();
    var that = this;
    console.log('this',this)
    var openid = wx.getStorageSync('LavandeOpenid')
    wx.showLoading({
      title: '加载中',
    })
    if (!openid) {
      wx.hideLoading()
      wx.showModal({
        title: '提示',
        content: '支付失败，获取不到用户信息',
        showCancel: false,
        success: function (sres) {
          
        }
      })
      return;
    }

    let params={
      platformCode: 'WECHAT_XCX_BEE',
      outTradeNo: this.data.orderInfo.orderNo,
      amount: this.data.amount,
      body: '预订' + this.data.roomInfo.roomTypeName + '1间' + this.data.cheakInDay + '晚' 
      + this.data.beginTime + '入住(联系人:' + this.data.guestlist[0].guestName + ')',
      notifyUrl: getApp().globalData.payNotifyUrl,
      attach: 'BEE-ZANGSHANG' + new Date().getTime(),
      openId: openid,
    }

    WechatDataAPI.actPayData( params, function (payDataRes) {
      wx.hideLoading()
      if (!payDataRes.success) {
        if (payDataRes.errMsg.indexOf('该订单已支付') > -1){
          that.payPlatform();
        }else{
          wx.showModal({
            title: '提示',
            content: payDataRes.errMsg,
            showCancel: false,
            success: function (showModalRes) { }
          })
          return;
        }
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
              that.payPlatform();
            }
          })
        },
        'fail': function (fres) {
          console.log(fres)
        }
      })
    });
  },
  payPlatform(){
    let that = this;
    let orderNo = this.data.orderInfo.orderNo;
    let updateTimes = 5;
    wx.showLoading({})
    platformAPI.updOrderStatus({
      orderNo: orderNo,
      status: 'I',
      accountCode: 'AR_wx'
    }, function (res) {
      wx.hideLoading()
      if (!res.success) {
        if (updateTimes == 0) {
          wx.showModal({
            title: '提示',
            content: '修改订单状态发生错误!' + res.errMsg,
            showCancel: false,
            success: function (res) {

            }
          })
          return;
        } else {
          updateTimes--;
          that.payPlatform();
        }
      } else {
        wx.setStorageSync("orderInfo", res.data);
        wx.navigateTo({
          url: '../checkDetail/checkDetail',
        })
      }
    });
  }


}
Page(Object.assign(orderPayData,{}))