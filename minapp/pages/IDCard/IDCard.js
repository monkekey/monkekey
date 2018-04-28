// pages/IDCard/IDCard.js
const platformAPI = require('../../api/PlatformAPI.js')

const app = getApp();

const idCardData = {
  data: {
    globalData: app.globalData,
    userInfo: app.globalData.userInfo,
    upCard: false,
  },

  onLoad: function (options) {
    let userInfo = wx.getStorageSync('LavandeUserInfo');
    let roomInfo = wx.getStorageSync("roomInfo", this.data.roomInfo);
    let orderInfo = wx.getStorageSync("orderInfo", this.data.orderInfo);
    let guestInfo = '';
    let orderNo = '';
    if (options.guestInfo){
      guestInfo = JSON.parse(options.guestInfo);
      console.log(guestInfo)
    }else{
      guestInfo = {
        orderNo: orderInfo.orderNo,
        roomCode: roomInfo.roomCode,
        status: 0,
        facePath:'',
        guestMobile:'',
        guestName:'',
        viewPath:''
      }
    }
    orderNo = orderInfo.orderNo;
    this.setData({ userInfo, guestInfo , orderNo})
  },

  onShow: function () {

  },

  IDCardPhoto(){
    console.log('上传身份证')
    var that = this;
    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册('album')还是相机('camera')，默认二者都有
      success: function (res) {
        // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
        console.log('res',res)
        var tempFilePaths = res.tempFilePaths;
        var tempFiles = res.tempFiles;
        that.uploadIDCard(tempFilePaths, 0);
      },
      fail: function (res){
        wx.showModal({
          title: '提示',
          content: res,
        })
      }
    })
  },
  onBill(){//确认
    if (this.data.upCard){
      wx.navigateBack({ delta: 1, })
    }else{
      this.setData({ upCard: true })
    }
  },
  uploadIDCard: function (files, i) {//递归调用
    var that = this;
    let orderNo = this.data.orderNo;
    let guestInfo = this.data.guestInfo;
    let token = getApp().getWxStorageSync('token');
    wx.showLoading({})
    wx.uploadFile({
      header: { "Content-Type": "application/json" }, // 设置请求的 header  --"Authorization": "Basic " + token 
      url: platformAPI.uploadIDCard + '?orderNo=' + orderNo, //仅为示例，非真实的接口地址
      filePath: files[i],
      name: 'uploadingFile',
      success: function (res) {
        wx.hideLoading();
        console.info(res);
        var json = JSON.parse(res.data)
        console.info('json',json);
        if (json.success){
            var data = json.data;
          console.info('data',data);
          guestInfo.viewPath = json.data.IDurl
          guestInfo.guestName = json.data.name
          guestInfo.guestPid = json.data.num
          that.setData({
            upCard: true,
            guestInfo
          })
          that.updGuest();
        }else{
          wx.showModal({
            title: '提示',
            content: json.data ? json.data:'身份证识别失败，请重新上传',
          })
        }
      },
      fail: function (res) {
        wx.hideLoading();
        wx.showModal({
          title: '提示',
          content: res,
        })
      }
    })
  },

  FacePhoto(){
    console.log('人脸识别')
    let that = this;
    wx.chooseImage({
      count: 1, // 默认9
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['camera'], // 可以指定来源是相册('album')还是相机('camera')，默认二者都有
      success: function (res) {
        console.log('res', res)
        var tempFilePaths = res.tempFilePaths;
        var tempFiles = res.tempFiles;
        that.uploadFace(tempFilePaths, 0);
      },
      fail: function (res) {
        console.log('fail', res)
      }
    })
  },
  uploadFace: function (files, i){
    var that = this;
    let orderNo = this.data.orderNo;
    let guestInfo = this.data.guestInfo;
    wx.showLoading({})
    wx.uploadFile({
      header: { "Content-Type": "application/json" }, // 设置请求的 header  --"Authorization": "Basic " + token 
      url: platformAPI.uploadFace + '?IDurl=' + guestInfo.viewPath + '&orderNo=' + orderNo, //仅为示例，非真实的接口地址
      filePath: files[i],
      name: 'uploadingFile',
      success: function (res) {
        wx.hideLoading();
        console.info(res);
        var json = JSON.parse(res.data)
        console.info('json', json);
        if (json.success) {
          var data = json.data;
          guestInfo.facePath = json.data.faceUrl;
          if (data.confidence>=70){//对比度大于70
            guestInfo.status = 1;
            that.setData({ guestInfo})
            that.updGuest();
          }else{
            wx.showModal({
              title: '提示',
              content: '匹配不通过，请重新上传正面照',
            })
          }
        }
      },
      fail: function (res) {
        wx.hideLoading();
        console.error(res);
      }
    })
  },
  updGuest(){
    let that = this;
    let guestInfo = this.data.guestInfo;
    platformAPI.updOrderGuest(guestInfo, function (res) {
      console.log(res)
      if (res.success) {
        if (guestInfo.status){
          wx.navigateBack({ delta: 1, })
        }else{
          that.setData({
            guestInfo:res.data
          })
        }
      }else{
        wx.showModal({
          title: '提示',
          content: '修改入住信息失败',
        })
        wx.navigateBack({ delta: 1, })
        return;
      }
    })
  }

}
Page(Object.assign(idCardData, {}))