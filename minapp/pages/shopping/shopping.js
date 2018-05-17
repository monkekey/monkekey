// pages/shop/index.js
//获取应用实例
var app = getApp()
var Zan = require('../../zanui/index');
const lavandeAPI = require('../../api/LavandeAPI.js')
const Request = require('../../utils/RequestUtil.js')
const tools = require('../../utils/tools.js')
var WechatDataAPI = require('../../api/WechatDataAPI.js');

const shoppingData = {
  data: {
    selectCurrent: 0,
    categories: [],
    activeCategoryId: 0,
    goods: [],
    scrollTop: "0",
    loadingMoreHidden: true,
    searchInput: '',
    totalCount: 0,
    totalPrice: 0,
    trHeight: 0,
    trWidth: 0,

    cartShow: 'none',
    carArray: [],
    fold: true,
    notices: [
    ],
    newCommodity: [],
    scrollHeight: 0,
    stepper3: {
      stepper: 0,
      min: 0,
      max: 99
    },
    countPrice:"0.00",
    countSum:0,
    category:true,
    queryCommodity:true,
    showBottomPopup: false,
    statistics:[],
    commodityList:[],
    queryInputVal:"",
    keyword:"",
    queryCommodityList:[],
    keywordPage:{
      pageIdx:0,
      pageSize:10,
      page:false
    },
    commodityPage: {
      pageIdx: 0,
      pageSize: 10,
      page: false
    },
    loading: false,
    account:"",
    fromData:"",
    hotelId:"",
    room:""
  },
  scroll: function (e) {
    var that = this, scrollTop = that.data.scrollTop;
    that.setData({
      scrollTop: e.detail.scrollTop
    })
  },
  onLoad: function (options) {
    wx.showLoading({
      title: '努力加载中',
    })
    var that = this
    that.setData({
      account: options.account,
      fromData: options.from,
      hotelId: options.hotelId,
      room: options.room
    })
    wx.getSystemInfo({
      success: (res) => {
        let height = res.windowHeight 
        this.setData({
          trHeight: res.windowHeight,
          trWidth: res.windowWidth,
          scrollHeight: height
        })
      }
    });

    var categories = [{ id: "", name: "推荐" }];
    categories.push({id:"query",name:"搜索"})
    lavandeAPI.getCategory({ hotelId: options.hotelId }, function (respon) {
      if (respon.success == true) {
        console.log(respon)
        let data = respon.data
        for(let i in data){
          categories.push({ name: data[i].categoryName, id: data[i].categoryCode });
        }
        that.setData({
          categories: categories
        });
      }
    })

    lavandeAPI.getNewCommodity({ hotelId: options.hotelId }, function (respon) {
      if (respon.success == true) {
        console.log(respon)
        let data = respon.data
        for(let i in data){
          data[i].stepper= {
              stepper: 0,
              min: 0,
              max: 99
            }
         
        }
        console.log(data)
        that.setData({
          newCommodity: data
        });
      }
    })

    lavandeAPI.getHotCommodity({ hotelId: options.hotelId }, function (respon) {
      if (respon.success == true) {
        console.log(respon)
        let data = respon.data
        for (let i in data) {
          data[i].stepper = {
            stepper: 0,
            min: 0,
            max: 99
          }

        }
        console.log(data)
        that.setData({
          hotCommodity: data
        });
      }
    })

    lavandeAPI.getLikeCommodity({ hotelId: options.hotelId }, function (respon) {
      if (respon.success == true) {
        console.log(respon)
        let data = respon.data
        for (let i in data) {
          data[i].stepper = {
            stepper: 0,
            min: 0,
            max: 99
          }

        }
        console.log(data)
        that.setData({
          likeCommodity: data
        });
      }
    })

    let images = [
      { url: 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/bg-auth1.jpg' },
      { url: 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/main_7.png' },
      { url: 'http://aploproducts.oss-cn-shenzhen.aliyuncs.com/main_8.png' }
    ]

    let hotCommodity = []

    let newCommodity = []

    let likeCommodity = []
    
    that.setData({
      // categories: categories,
      activeCategoryId: 0,
      notices: images,
    });
    that.getGoodsList(0);
    wx.hideLoading()
  },

  getGoodsList: function (categoryId) {
    if (categoryId == 0) {
      categoryId = "";
    }
    var that = this;

    that.setData({
      goods: [],
      loadingMoreHidden: false
    });

    var goods = [];

    if (goods.length > 0) {
      that.setData({
        goods: goods,
        loadingMoreHidden: true
      });
    } else {
      that.setData({
        goods: goods,
        loadingMoreHidden: false
      });
    }
  },
  tabClick: function (e) {
    let categoryCode = e.currentTarget.id
    
    let that = this
    let showCategory = true
    let queryCategory = true
    if (e.currentTarget.id == ''){
      showCategory = true
      queryCategory = true
    } else if (e.currentTarget.id == 'query' ){
      showCategory = true
      queryCategory = false
    }else{
      showCategory = false
      queryCategory = true
    }

    let page = {
      pageIdx: 0,
      pageSize: 10,
      page: false
    }

    this.setData({
      category: showCategory,
      queryCommodity: queryCategory,
      activeCategoryId: e.currentTarget.id,
      commodityPage: page,
      // loading:false
    });
    if (categoryCode == "query"){
      return
    }
    that.queryCommodity()
  },
  queryCommodity(){
    let that = this
    let hotelId = that.data.hotelId
    let page = that.data.commodityPage
    if (page.page) return;
    let idx = page.pageIdx
    let size = page.pageSize
    let loading = that.data.loading;
    if (loading) return;
    that.setData({ loading: true })
    let categoryCode = that.data.activeCategoryId
    lavandeAPI.getCommodity({ hotelId: hotelId, categoryCode: categoryCode, pageIdx: idx, pageSize: size }, function (respon) {
      that.setData({ loading: false })
      if (respon.success == true) {
        let data = respon.data
        let dataList = that.data.statistics
        if (dataList.length > 0) {
          for (let i in dataList) {
            if (i == "sum") {
              break;
            }
            for (let j in data) {
              if (dataList[i].commodityCode == data[j].commodityCode) {
                data[j].stepper = dataList[i].stepper
              } else {
                data[j].stepper = {
                  stepper: 0,
                  min: 0,
                  max: 99
                }
              }
            }
          }
        } else {
          for (let i in data) {
            data[i].stepper = {
              stepper: 0,
              min: 0,
              max: 99
            }
          }
        }

        page.pageIdx = (page.pageIdx++) * 10
        page.page = data.length < page.pageSize || data.length == page.pageSize  ? true : false
        console.log(page)
        let commodityList = that.data.commodityList
        commodityList.push(...data)

        that.setData({
          commodityList: data,
          commodityPage: page
        });
      }
    })
  },
  toDetailsTap: function (e) {
  },


  pay: function () {
    let that = this
    if (this.data.carArray.length > 0) {
      wx.showModal({
        title: '提示',
        content: '确认提交订单？',
        success: function (res) {
          if (res.confirm) {
            getApp().setWxStorageSync('ShopDetail', JSON.stringify(that.data.carArray));
            getApp().setWxStorageSync('OrderNo', "YWRW21831");

            wx.navigateBack({
              delta: 1
            })
          } else if (res.cancel) {
            wx.getStorageSync('ShopDetail') && wx.removeStorageSync('ShopDetail');
            wx.getStorageSync('OrderNo') && wx.removeStorageSync('OrderNo');
          }
        }
      })
    }
  },
  onShareAppMessage: function () {
    return {
      title: wx.getStorageSync('mallName') + '——' + app.globalData.shareProfile,
      path: '/pages/index/index',
      success: function (res) {
        // 转发成功
      },
      fail: function (res) {
        // 转发失败
      }
    }
  },
  handleZanStepperChange(e) {
    var componentId = e.componentId;
    var stepper = e.stepper;
    let newCommodity = this.data.newCommodity;
    newCommodity[componentId].stepper.stepper = stepper;
    this.setData({
      // [`${componentId}.stepper`]: stepper
      newCommodity
    });
  },
  //增加商品
  addcommodity(e){
    let _this = this
    let index = e.currentTarget.dataset.index
    let listname = e.currentTarget.dataset.list
    let initList = [];
    if (listname == 'hotCommodity'){
      initList = _this.data.hotCommodity
      if (initList[index].stepper.stepper > 99) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper + 1
      _this.setData({
        hotCommodity: initList
      });
    } else if (listname == 'likeCommodity'){
      initList = _this.data.likeCommodity
      if (initList[index].stepper.stepper > 99) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper + 1
      _this.setData({
        likeCommodity: initList
      });
    } else if (listname == 'newCommodity') {
      initList = _this.data.newCommodity
      if (initList[index].stepper.stepper > 99) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper + 1
      _this.setData({
        newCommodity: initList
      });
    } else if (listname == 'typeCommodity') {
      initList = _this.data.commodityList
      if (initList[index].stepper.stepper > 99) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper + 1
      _this.setData({
        commodityList: initList
      });
    } else if (listname == 'queryCommodity') {
      initList = _this.data.queryCommodityList
      if (initList[index].stepper.stepper > 99) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper + 1
      _this.setData({
        queryCommodityList: initList
      });
    }

    
    let commodity = {}
    commodity = e.currentTarget.dataset.commodityinfo
    let statisticsCommodity = _this.data.statistics
    let exist = true
    if (statisticsCommodity.length > 0){
      for (let i in statisticsCommodity){
        if (statisticsCommodity[i].commodityCode == commodity.commodityCode){
          statisticsCommodity[i].commoditySum = statisticsCommodity[i].commoditySum + 1
          statisticsCommodity[i].pirceSum = statisticsCommodity[i].pirceSum + statisticsCommodity[i].commodityPrice
          statisticsCommodity[i].stepper.stepper = statisticsCommodity[i].commoditySum
          statisticsCommodity.sum = 1 + statisticsCommodity.sum
          statisticsCommodity.price = statisticsCommodity[i].commodityPrice + statisticsCommodity.price
          exist = false
          break
        }
      }
      if (exist){
        commodity.commoditySum = 1
        commodity.pirceSum = commodity.commodityPrice
        commodity.stepper.stepper = commodity.commoditySum
        statisticsCommodity.push(commodity)
        statisticsCommodity.sum = 1 + statisticsCommodity.sum
        statisticsCommodity.price = commodity.commodityPrice + statisticsCommodity.price
      }
    }else{
      commodity.commoditySum = 1
      commodity.pirceSum = commodity.commodityPrice
      commodity.stepper.stepper = commodity.commoditySum
      statisticsCommodity.push(commodity)
      statisticsCommodity.sum = 1
      statisticsCommodity.price = commodity.commodityPrice
    }
    
    _this.setData({
      statistics: statisticsCommodity,
      countPrice: (statisticsCommodity.price).toFixed(2),
      countSum: statisticsCommodity.sum,
    })
    console.log(_this.data.statistics)

  },
  //减少商品
  subcommodity(e){
    let _this = this
    let index = e.currentTarget.dataset.index
    let listname = e.currentTarget.dataset.list
    let initList = [];
    if (listname == 'hotCommodity') {
      initList = _this.data.hotCommodity
      if (initList[index].stepper.stepper < 1){
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper - 1
      
      _this.setData({
        hotCommodity: initList
      });
    } else if (listname == 'likeCommodity') {
      initList = _this.data.likeCommodity
      if (initList[index].stepper.stepper < 1) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper - 1
      _this.setData({
        likeCommodity: initList
      });
    } else if (listname == 'newCommodity') {
      initList = _this.data.newCommodity
      if (initList[index].stepper.stepper < 1) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper - 1
      _this.setData({
        newCommodity: initList
      });
    } else if (listname == 'typeCommodity') {
      initList = _this.data.commodityList
      if (initList[index].stepper.stepper < 1) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper - 1
      _this.setData({
        commodityList: initList
      });
    } else if (listname == 'queryCommodity') {
      initList = _this.data.queryCommodityList
      if (initList[index].stepper.stepper < 1) {
        return
      }
      initList[index].stepper.stepper = initList[index].stepper.stepper - 1
      _this.setData({
        queryCommodityList: initList
      });
    }

    let commodity = {}
    commodity = e.currentTarget.dataset.commodityinfo
    let statisticsCommodity = _this.data.statistics
    let exist = true
    if (statisticsCommodity.length > 0) {
      for (let i in statisticsCommodity) {
        if (statisticsCommodity[i].commodityCode == commodity.commodityCode) {
          statisticsCommodity[i].commoditySum = statisticsCommodity[i].commoditySum - 1
          statisticsCommodity[i].pirceSum = statisticsCommodity[i].pirceSum - statisticsCommodity[i].commodityPrice
          statisticsCommodity[i].stepper.stepper = statisticsCommodity[i].commoditySum
          statisticsCommodity.sum = statisticsCommodity.sum - 1
          statisticsCommodity.price = statisticsCommodity.price - statisticsCommodity[i].commodityPrice
          exist = false
          break
        }
      }
    }

    _this.setData({
      statistics: statisticsCommodity,
      countPrice: (statisticsCommodity.price).toFixed(2),
      countSum: statisticsCommodity.sum,
    })
    console.log(_this.data.statistics)

  },

  //减少购物车
  subCard(e){

    let likeCommodity = this.data.likeCommodity
    let newCommodity = this.data.newCommodity
    let hotCommodity = this.data.hotCommodity
    let commodityList = this.data.commodityList
    let queryCommodityList = this.data.queryCommodityList

    let statistics = this.data.statistics
    let idx = e.currentTarget.dataset.index
    let cardCommodity = e.currentTarget.dataset.commodityinfo

    cardCommodity.commoditySum = cardCommodity.commoditySum - 1
    cardCommodity.pirceSum = cardCommodity.pirceSum - cardCommodity.commodityPrice
    cardCommodity.stepper.stepper = cardCommodity.commoditySum

    statistics.sum = statistics.sum - 1
    statistics.price = statistics.price - cardCommodity.commodityPrice

    if (cardCommodity.stepper.stepper == 0) {
      statistics.splice(idx, 1)
    } else {
      statistics[idx] = cardCommodity
    }

      for(let l in likeCommodity){
        if (cardCommodity.commodityCode == likeCommodity[l].commodityCode){
          likeCommodity[l].stepper.stepper = likeCommodity[l].stepper.stepper - 1
          break;
        }
      }
      for(let n in newCommodity){
        if (cardCommodity.commodityCode == newCommodity[n].commodityCode) {
          newCommodity[n].stepper.stepper = newCommodity[n].stepper.stepper - 1
          break;
        }
      }
      for(let h in hotCommodity){
        if (cardCommodity.commodityCode == hotCommodity[h].commodityCode) {
          hotCommodity[h].stepper.stepper = hotCommodity[h].stepper.stepper - 1
          break;
        }
      }

      for (let c in commodityList) {
        if (cardCommodity.commodityCode == commodityList[c].commodityCode) {
          commodityList[c].stepper.stepper = commodityList[c].stepper.stepper - 1
          break;
        }
      }

      for (let q in queryCommodityList) {
        if (cardCommodity.commodityCode == queryCommodityList[q].commodityCode) {
          queryCommodityList[q].stepper.stepper = queryCommodityList[q].stepper.stepper - 1
          break;
        }
      }

    this.setData({
      statistics: statistics,
      countPrice: (statistics.price).toFixed(2),
      countSum: statistics.sum,
      likeCommodity: likeCommodity,
      newCommodity: newCommodity,
      hotCommodity: hotCommodity,
      commodityList: commodityList,
      queryCommodityList: queryCommodityList
    })

  },


  //增加购物车
  addCard(e){

    let likeCommodity = this.data.likeCommodity
    let newCommodity = this.data.newCommodity
    let hotCommodity = this.data.hotCommodity
    let commodityList = this.data.commodityList
    let queryCommodityList = this.data.queryCommodityList

    let statistics = this.data.statistics
    let idx = e.currentTarget.dataset.index
    let cardCommodity = e.currentTarget.dataset.commodityinfo

    cardCommodity.commoditySum = cardCommodity.commoditySum + 1
    cardCommodity.pirceSum = cardCommodity.pirceSum + cardCommodity.commodityPrice
    cardCommodity.stepper.stepper = cardCommodity.commoditySum

    statistics[idx] = cardCommodity
    statistics.sum = statistics.sum + 1
    statistics.price = statistics.price + cardCommodity.commodityPrice

    for (let l in likeCommodity) {
      if (cardCommodity.commodityCode == likeCommodity[l].commodityCode) {
        likeCommodity[l].stepper.stepper = likeCommodity[l].stepper.stepper + 1
        break;
      }
    }
    for (let n in newCommodity) {
      if (cardCommodity.commodityCode == newCommodity[n].commodityCode) {
        newCommodity[n].stepper.stepper = newCommodity[n].stepper.stepper + 1
        break;
      }
    }
    for (let h in hotCommodity) {
      if (cardCommodity.commodityCode == hotCommodity[h].commodityCode) {
        hotCommodity[h].stepper.stepper = hotCommodity[h].stepper.stepper + 1
        break;
      }
    }
    for (let c in commodityList) {
      if (cardCommodity.commodityCode == commodityList[c].commodityCode) {
        commodityList[c].stepper.stepper = commodityList[c].stepper.stepper + 1
        break;
      }
    }
    for (let q in queryCommodityList) {
      if (cardCommodity.commodityCode == queryCommodityList[q].commodityCode) {
        queryCommodityList[q].stepper.stepper = queryCommodityList[q].stepper.stepper + 1
        break;
      }
    }

    this.setData({
      statistics: statistics,
      countPrice: (statistics.price).toFixed(2),
      countSum: statistics.sum,
      likeCommodity: likeCommodity,
      newCommodity: newCommodity,
      hotCommodity: hotCommodity,
      commodityList: commodityList,
      queryCommodityList: queryCommodityList
    })

  },

  //取消订单时，恢复库存
  cleanOrder: function(){
    let _this = this
    var hotelId = _this.data.hotelId
    var account = _this.data.account
    var fromData = _this.data.fromData
    var room = _this.data.room
    var commodityList = _this.data.statistics

    let admireInfo = {
      hotelId: hotelId,
      room: room,
      wxOrderDetailVOS: commodityList
    }

    lavandeAPI.cleanOrder(JSON.stringify(admireInfo), function (respon) {
      console.log(respon)
      if (respon.success == true) {
        
      }

    })

  },

  //支付
  payOrder: function () {
    console.log(this.data)
    let list = this.data.statistics
    if (list == 0){
      return
    }
    var userinfo = wx.getStorageSync('LavandeUserInfo');
    var order = 'Bee-yanxuan' + new Date().getTime();
    var _this = this;
    
    var hotelId = _this.data.hotelId
    var account = _this.data.account
    var fromData = _this.data.fromData
    var room = _this.data.room
    
    var countPrice = _this.data.countPrice 
    var countSum = _this.data.countSum
    
    var openid = wx.getStorageSync('LavandeOpenid')
    
    wx.showLoading({
      title: '加载中',
    })

    var commodityList = _this.data.statistics

    let admireInfo = {
      hotelId: hotelId,
      room: room,
      wxOrderDetailVOS: commodityList
    }

    //库存检查
    lavandeAPI.checkStorage(JSON.stringify(admireInfo), function (respon) {
      console.log(respon)
      if (respon.success == true) {
        if (respon.data != 'ok'){
          wx.hideLoading()
          wx.showModal({
            title: '提示',
            content: respon.data,
            showCancel: false,
            success: function (res) {
              
            }
          })
        }else{
          if (!openid) {
            wx.hideLoading()
            wx.showModal({
              title: '提示',
              content: '支付失败，获取不到用户信息，请退出重新进入',
              showCancel: false,
              success: function (sres) {
                // wx.switchTab({
                //   url: '../pages/pay/pay?price =' + _this.data.price + " & username=" + _this.data.userName
                // })
              }
            })
            return;
          }

          lavandeAPI.pay({
            // platformCode: getApp().globalData.platformCode,
            platformCode: 'Hanpudun',
            openid: openid,
            body: userinfo.nickName +'购买商品共计:' + countPrice + "元",
            orderNo: order,
            notifyUrl: getApp().globalData.payNotifyUrl,
            attach: 'HanpudunShop' + new Date().getTime(),
            // total_fee: countPrice,
            total_fee: 0.01,
            create_ip:'',
            tradeType:'JSAPI'
            
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
            var payData = payDataRes.data;
            console.log('payDataRes', payData)
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
                      createBy: userinfo.nickName,
                      createTime: new Date(),
                      hotelId: hotelId,
                      openId: openid,
                      payNumber: order,
                      price: countPrice,
                      room: room,
                      sum: countSum,
                      wxOrderDetailVOS: _this.data.statistics
                    }
                    lavandeAPI.savePayment(JSON.stringify(admireInfo), function (respon) {
                      console.log(respon)
                      if (respon.success == true) {
                        getApp().setWxStorageSync('ShopDetail', '您有新的订单，请抓紧时间配送，订单编号为：');
                        getApp().setWxStorageSync('OrderNo', respon.data.orderNumber);

                        wx.navigateBack({
                          delta: 1
                        })
                      }

                    })
                  }
                })
              },
              'fail': function (fres) {
                console.log(fres)
                _this.cleanOrder()
              }
            })
          });
        }
      }

    })

   
  },

  //搜索输入框监听
  bindKeyInput(e){
    this.setData({
      queryInputVal: e.detail.value
    })
  },
  clickKeyword(){
    let that = this
    let keyword = that.data.queryInputVal
    let pageData = {
      pageIdx: 0,
      pageSize: 10,
      page: false
    }
    that.setData({
      keyword: keyword,
      keywordPage: pageData,
      activeCategoryId: "query",
      category: true,
      queryCommodity: false,
    })
    that.queryKeyword()
  },
  //根据关键字搜索
  queryKeyword(){
    
    let that = this
    let hotelId = that.data.hotelId
    let keyword = that.data.keyword
    let pageData = that.data.keywordPage
    if (pageData.page)return;
    let idx = pageData.pageIdx
    let size = pageData.pageSize
    let loading = this.data.loading;
    if(loading) return;
    this.setData({loading:true})
    wx.showLoading({
      title: '搜索中...',
    })
    lavandeAPI.getKeywordCommodity({ hotelId: hotelId, keyword: keyword, pageIdx: idx, pageSize: size }, function (respon) {
      that.setData({ loading: false })
      if (respon.success == true) {
        let data = respon.data
        console.log(data)
        
        let dataList = that.data.statistics
        console.log(dataList)
        if (dataList.length > 0) {
          for (let i in dataList) {
            if (i == "sum") {
              break;
            }
            for (let j in data) {
              if (dataList[i].commodityCode == data[j].commodityCode) {
                data[j].stepper = dataList[i].stepper
              } else {
                data[j].stepper = {
                  stepper: 0,
                  min: 0,
                  max: 99
                }
              }
            }
          }
        } else {
          for (let i in data) {
            data[i].stepper = {
              stepper: 0,
              min: 0,
              max: 99
            }
          }
        }
        pageData.pageIdx = (pageData.pageIdx++) * 10
        pageData.page = data.length < pageData.pageSize ? true : false

        let queryCommodityList = that.data.queryCommodityList
        queryCommodityList.push(...data)

        that.setData({

          queryCommodityList: queryCommodityList,
          keywordPage: pageData
        });
        wx.hideLoading()
      }else{
          wx.hideLoading()
          wx.showToast({
            title: '没有更多商品',
            icon: 'none'
          })
      }
    })
  },
  //滚动到底部
  lower: function (e) {
    let that = this
    let code = that.data.activeCategoryId 
    if (code == 'query'){
      that.queryKeyword()
    } else if (code != '' && code != 'query'){
      console.log('222')
      that.queryCommodity()
    }
  },
  skipOrder(){
    wx.navigateTo({
      url: "/pages/order/index"
    })
  },
  skipIndex() {
    wx.getStorageSync('ShopDetail') && wx.removeStorageSync('ShopDetail');
    wx.getStorageSync('OrderNo') && wx.removeStorageSync('OrderNo');
    wx.navigateBack({
      delta: 1
    })
  },
  toggleBottomPopup() {
    this.setData({
      showBottomPopup: !this.data.showBottomPopup
    });
  },
  //清空购物车
  cleanCard() {
    let that = this
    let commodity = that.data.statistics
    let likeCommodity = that.data.likeCommodity
    let newCommodity = that.data.newCommodity
    let hotCommodity = that.data.hotCommodity
    let commodityList = that.data.commodityList
    let queryCommodityList = that.data.queryCommodityList
    console.log(commodity)
    for (let l in likeCommodity){
      likeCommodity[l].stepper = {
        stepper: 0,
        min: 0,
        max: 99
      }
    }
    for (let n in newCommodity) {
      newCommodity[n].stepper = {
        stepper: 0,
        min: 0,
        max: 99
      }
    }
    for (let h in hotCommodity) {
      hotCommodity[h].stepper = {
        stepper: 0,
        min: 0,
        max: 99
      }
    }
    for (let c in commodityList) {
      commodityList[c].stepper = {
        stepper: 0,
        min: 0,
        max: 99
      }
    }
    for (let q in queryCommodityList) {
      queryCommodityList[q].stepper = {
        stepper: 0,
        min: 0,
        max: 99
      }
    }
    that.setData({
      statistics:[],
      likeCommodity:likeCommodity,
      newCommodity:newCommodity,
      hotCommodity:hotCommodity,
      commodityList:commodityList,
      queryCommodityList:queryCommodityList,
      countPrice: "0.00",
      countSum: 0,
    })
  },
  
}
Page(Object.assign(shoppingData, {}, Zan.Stepper))