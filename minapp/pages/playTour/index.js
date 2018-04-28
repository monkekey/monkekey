const lavandeAPI = require('../../api/LavandeAPI.js')
const Request = require('../../utils/RequestUtil.js')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    web_base_url: Request.BASE_URL,
    userName:'',
    scenes:'',
    scene:[],
    count:0,
    imgUrl:'',
    imageUrl:'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/xiaoer.png'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let self = this
    let scene = decodeURIComponent(options.scenes).split("_")
    lavandeAPI.getCount({ innCode: scene[0], name: options.username }, function (respon) {
      if (respon.success == true) {
        self.setData({
          count: respon.data,
        })
      }
    })
    self.setData({
      userName: options.username,
      scenes: options.scenes,
      imageUrl: options.imgUrl ? options.imgUrl + "?" + Math.random() / 9999 : 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/xiaoer.png',
      scene: scene,
      imgUrl: options.imgUrl
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
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  },

  /**
   * 选择赞赏金额
   */
  operationPrice: function(event){
    let price = parseInt(event.currentTarget.dataset.price)
    let username = this.data.userName
    wx.navigateTo({
      url: "/pages/pay/pay?price=" + price + "&username=" + username + "&scenes=" + this.data.scenes + "&imgUrl=" + this.data.imgUrl
    })
  }
})