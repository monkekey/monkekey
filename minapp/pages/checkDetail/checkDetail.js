// pages/checkDetail/checkDetail.js
const app = getApp();
const platformAPI = require('../../api/PlatformAPI');
const Moment = require('../../utils/Moment.js')
const checkDetailData = {
  data: {
    globalData: app.globalData,
    userInfo: app.globalData.userInfo,
    roomInfo: '',
    orderInfo:''
  },

  onLoad: function (options) {
    let userInfo = wx.getStorageSync('LavandeUserInfo');
    let roomInfo = wx.getStorageSync('roomInfo');
    let orderInfo = wx.getStorageSync('orderInfo');
    let estimateBeginTime = Moment(orderInfo.estimateBeginTime.replace(/\-/, '/')).format('yyyy/MM/dd');
    let estimateEndTime = Moment(orderInfo.estimateEndTime.replace(/\-/, '/')).format('yyyy/MM/dd');
    let cheakInDay = Moment(estimateEndTime.replace(/\-/g, '/')).differ(estimateBeginTime.replace(/\-/g, '/'))
    this.setData({ userInfo, roomInfo, orderInfo, estimateBeginTime, estimateEndTime, cheakInDay })
    this.getOrderDatiel();
  },

  onShow: function () {

  },

  clickButton:function(){//获取门锁密码
    let params = {
      roomCode: this.data.roomInfo.roomCode,
      ownerMobile: '13800000000',
      lockType: 'dding',
      orderType: 'SK',
      checkinTime: this.data.orderInfo.estimateBeginTime,
      checkoutTime: this.data.orderInfo.estimateEndTime,
      orderNo: this.data.orderInfo.orderNo
    }
    platformAPI.getLockpwd(params,function(res){
      console.log(res)
      if(res.success){
        wx.showModal({
          title: '提示',
          content: res.data,
        })
      }else{
        wx.showModal({
          title: '提示',
          content: res.errMsg,
        })
      }
    })
  },
  // clickButton1: function () {//获取门锁动态密码
  //   let params = {
  //     roomCode: this.data.roomInfo.roomCode,
  //     ownerMobile: '13800000000',
  //     lockType: 'dding',
  //     orderType: 'SK',
  //     orderNo: this.data.orderInfo.orderNo
  //   }
  //   platformAPI.getDynamicPwd(params,function(res){
  //     console.log(res)
  //   })
  // },
 
  getOrderDatiel: function () {//入住人信息
    let orderNo = this.data.orderInfo.orderNo;
    let that = this;
    wx.showLoading({})
    platformAPI.getOrderDetail({ orderNo: orderNo }, function (res) {
      wx.hideLoading();
      if (res.success) {
        let guestlist = res.data.orderGuestList[0];
        that.setData({ guestlist })
      } else {
        wx.showModal({
          title: '提示',
          content: res.errMsg ? res.errMsg : '',
          showCancel: false,
          success: function (res) {
          }
        })
        return;
      }
    })
  },
  callKp(){
    console.log('呼叫管家')
    let hotelId = '358858000000000';
    let roomNo = this.data.roomInfo.roomNo;
    let scene = hotelId + '_' + roomNo;
    wx.reLaunch({
      url: '../index/index?scene=' + scene,
    })
  },
  onBill(){
    wx.reLaunch({
      url: '../roomChange/roomChange',
    })
  }

}
Page(Object.assign(checkDetailData,{}))