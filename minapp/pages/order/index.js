// pages/order/index.js
var app = getApp()
var Zan = require('../../zanui/index');
const lavandeAPI = require('../../api/LavandeAPI.js')
const Request = require('../../utils/RequestUtil.js')
const Moment = require('../../utils/Moment.js')
var WechatDataAPI = require('../../api/WechatDataAPI.js');

const orderData = {

  /**
   * 页面的初始数据
   */
  data: {
    height: 0,
    tagNum:0,
    pageData:{
      pageIdx:0,
      pageSize:5
    },
    orderList:[],
    orderID:0,
    orderDateil:[],
    tab1: {
      list: [{
        id: 0,
        title: '已支付'
      }, {
        id: 1,
        title: '配送中'
      }, {
        id: 2,
        title: '待确认'
      }, {
        id: 3,
        title: '已完成'
      }],
      selectedId: 0
    },
    loading:false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.getSystemInfo({
      success: (res) => {
        this.setData({
          height: res.windowHeight
        })
      }
    });
    this.queryOrderList()
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
  handleZanTabChange(e) {
    var componentId = e.componentId;
    var selectedId = e.selectedId;
    this.setData({
      [`${componentId}.selectedId`]: selectedId
    });
    let num = selectedId
    let _this = this
    let page = _this.data.pageData
    page.pageIdx = 0
    _this.setData({
      tagNum: num,
      orderList:[],
      pageData: page
    })

    _this.queryOrderList()
  },
  queryOrderList: function(){
    if (this.data.selectedId){

    }
    wx.showLoading({
      title: '加载中',
    })
    let that = this
    let loading = that.data.loading;
    if (loading) return;
    that.setData({ loading: true })
    var userinfo = wx.getStorageSync('LavandeUserInfo');
    let openid = wx.getStorageSync('LavandeOpenid')
    let data = {
      openId: openid,
      username: userinfo.nickName,
      status: that.data.tagNum,
      pageIdx:that.data.pageData.pageIdx,
      pageSize: that.data.pageData.pageSize
    }
    lavandeAPI.getOrderListWx(data, function (respon) {
      that.setData({ loading: false })
      if (respon.success == true) {
        let data = respon.data
        for(let i in data){
          data[i].orderCreatetime = Moment(data[i].orderCreatetime).format('yyyy/MM/dd HH:mm:ss')
        }
        if (data.length == 0){
          wx.hideLoading()
          wx.showToast({
            title: '没有更多订单',
            icon: 'none'
          })
          return
        }
        let dataList = that.data.orderList
        for (let i in data){
          dataList.push(data[i])
        }
        
        that.setData({
          orderList: dataList
        });
        wx.hideLoading()
      }else{
        wx.hideLoading()
      }
    })
  },
  notarize: function(e){
    wx.showLoading({
      title: '确认中',
    })
    let orderNum = e.currentTarget.dataset.orderno
    let data = {
      orderNo: orderNum,
      status: 3
    }
    lavandeAPI.getOrderStatusWx(data, function (respon) {
      if (respon.success == true) {
        wx.hideLoading()
        wx.showToast({
          title: '确认成功',
          icon: 'success',
          duration: 2000
        })
        this.queryOrderList()
      }else{
        wx.hideLoading()
        wx.showToast({
          title: '操作失败',
          icon: 'none',
          duration: 2000
        })
      }
    })
  },
  scroll: function (e) {
    var that = this, scrollTop = that.data.scrollTop;
    that.setData({
      scrollTop: e.detail.scrollTop
      
    })
  },
  lower: function(e){
    let that = this
    let page = that.data.pageData
    page.pageIdx = page.pageIdx + 1
    that.setData({
      pageData: page
    })
    that.queryOrderList()
  }
}

Page(Object.assign(orderData, {}, Zan.Tab))