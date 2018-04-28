// pages/cheakin/cheakin.js
const app = getApp();
const platformAPI = require('../../api/PlatformAPI.js');
const Moment = require('../../utils/Moment.js')
const cheakinData = {
  data: {
    globalData: app.globalData,
    userInfo: app.globalData.userInfo,
    roomInfo: {},
    guestlist:[]
  },

  onLoad: function (options) {
    let userInfo = wx.getStorageSync('LavandeUserInfo');

    let roomInfo = wx.getStorageSync("roomInfo", this.data.roomInfo);
    let orderInfo = wx.getStorageSync("orderInfo", this.data.orderInfo);
    let orderPrice = wx.getStorageSync("orderPrice")
    let beginTime = orderPrice.checkInDate;
    let endTime = orderPrice.checkOutDate;
    let cheakInDay = Moment(endTime).differ(beginTime)
      var datearr1 = beginTime.split("-")
      var datearr2 = endTime.split("-")
      beginTime = datearr1[1] + '/' + datearr1[2];
      endTime = datearr2[1] + '/' + datearr2[2];
    console.log(endTime)
    this.setData({ userInfo, beginTime, endTime, cheakInDay, roomInfo, orderInfo})
    
  },

  onShow: function () {
    this.getOrderDatiel();
  },

  getOrderDatiel: function () {//入住人信息
    let orderNo = this.data.orderInfo.orderNo;
    // let orderNo = 'SK2018040409522548700';
    let that = this;
    wx.showLoading({})
    platformAPI.getOrderDetail({ orderNo: orderNo }, function (res) {
      wx.hideLoading();
      if (res.success) {
        let guestlist = res.data.orderGuestList;
        for (var i in guestlist) {
          if (guestlist[i].status == 1){
            guestlist[i].proving = true;
          }else{
            guestlist[i].proving = false;
          }
          guestlist[i].updM = false;
        }
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
  upper: function () {
    console.log('滚到最上方')
  },
  lower: function () {
    console.log('滚到最下方')
  },
  onSwiperChange(){

  },
  onIDCard(e){
    console.log(e)
    let item = e.currentTarget.dataset.item;
    let guestInfo = JSON.stringify(item);
    wx.navigateTo({
      url: '../IDCard/IDCard?guestInfo=' + guestInfo,
    })
  },
  addGuest(){
    console.log('添加入住人')
    wx.navigateTo({
      url: '../IDCard/IDCard',
    })
  },
  updMobile(e){
    let index = e.currentTarget.dataset.index;
    let guestlist = this.data.guestlist;
    guestlist[index].updM = true;
    this.setData({ guestlist})
  },
  //修改手机
  chgMobile(e){
    console.log(e)
    let that = this;
    let index = e.currentTarget.dataset.index;
    let value = e.detail.value;
    let guestlist = this.data.guestlist;

    var regex = new RegExp('^[1][0-9]{10}$','g');
    if (!regex.exec(value)){
      wx.showModal({
        title: '提示',
        content: '手机号格式不正确',
      })
    }else{
      guestlist[index].guestMobile = value;
      var guest = guestlist[index]
      platformAPI.updOrderGuest(guest, function(res){
        if(!res.success){
          wx.showModal({
            title: '提示',
            content: '修改入住信息失败',
          })
        }
        that.getOrderDatiel();
      })
    }
  },

  onBill(){
    console.log('确认')
    let that = this;
    let guestlist = this.data.guestlist;
    for (var i in guestlist){
      if (!guestlist[i].guestMobile){
        wx.showModal({
          title: '提示',
          content: '请输入手机号',
        })
        return;
      }
      if (guestlist[i].status != 1) {
        wx.showModal({
          title: '提示',
          content: '请验证身份',
        })
        return;
      }
    }
    let orderInfo = this.data.orderInfo;
    let param = {
      orderNo: orderInfo.orderNo,//'SK2018040422500193600'
      platformCode: 'WECHAT_XCX_BEE',//'WECHAT_XCX_YUMIHUI'
    }
    wx.showLoading({})
    platformAPI.getPayInfo( param, function(res){
      console.log(res)
      wx.hideLoading()
      if(res.success){
        if (res.data.tradeState == 'SUCCESS'){
          console.log('已支付')
          if (orderInfo.status == 'I'){
            wx.navigateTo({
              url: '../checkDetail/checkDetail',
            })
          }else{
            that.payPlatform()
          }
          return
        }
      }
      console.log('未支付')
      wx.navigateTo({
        url: '../orderPay/orderPay',
      })

    })
  },
  payPlatform() {
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

Page(Object.assign(cheakinData,{}))