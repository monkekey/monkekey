// pages/indexPage/indexPage.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    trHeight: 0,
    trWidth: 0,
    sences: "",
    hotelId: "",
    room:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options.scene)
    
    var scenes = decodeURIComponent(options.scene).split("_");

    wx.getSystemInfo({
      success: (res) => {
        let height = res.windowHeight
        this.setData({
          trHeight: res.windowHeight,
          trWidth: res.windowWidth,
          sences: scenes,
          hotelId: scenes[0],
          room: scenes[1]
        })
      }
    });

    
    if (scenes.length >= 2) {
      wx.redirectTo({
        url: '../index/index?scene=' + options.scene,
      })
      
    } else if (options.scene && scenes.length == 1) {
      console.log('scenes', scenes)
      //华美国际店
      // wx.reLaunch({
      //   url: '../roomChange/roomChange',
      // })
      wx.redirectTo({
        url: '../roomChange/roomChange',
      })
    }


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
  sikpRoom: function(){
    wx.redirectTo({
      url: '../roomChange/roomChange',
    })
  }
})