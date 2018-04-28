// pages/shop/index.js
//获取应用实例
var app = getApp()
Page({
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
  },
  scroll: function (e) {
    //  console.log(e) ;
    var that = this, scrollTop = that.data.scrollTop;
    that.setData({
      scrollTop: e.detail.scrollTop
    })
    // console.log('e.detail.scrollTop:'+e.detail.scrollTop) ;
    console.log('scrollTop:'+scrollTop)
  },
  onLoad: function () {
    var that = this
    // wx.request({
    //   url: 'https://api.it120.cc/' + app.globalData.subDomain + '/shop/goods/category/all',
    //   success: function (res) {
    //     var categories = [{ id: 0, name: "全部" }];
    //     if (res.data.code == 0) {
    //       for (var i = 0; i < res.data.data.length; i++) {
    //         categories.push(res.data.data[i]);
    //       }
    //     }
    //     that.setData({
    //       categories: categories,
    //       activeCategoryId: 0
    //     });
    //     that.getGoodsList(0);
    //   }
    // })

    wx.getSystemInfo({
      success: (res) => {
        this.setData({
          trHeight: res.windowHeight,
          trWidth: res.windowWidth
        })
      }
    });

    var categories = [{ id: "", name: "全部" }];
    categories.push({ name: "零食", id: "IT001" });
    categories.push({ name: "饮料", id: "IT002" });
    categories.push({ name: "日用", id: "IT003" });
    categories.push({ name: "保健", id: "IT004" });
    categories.push({ name: "计生", id: "IT005" });
    categories.push({ name: "其他", id: "IT006" });

    that.setData({
      categories: categories,
      activeCategoryId: 0
    });
    that.getGoodsList(0);
  },
  
  getGoodsList: function (categoryId) {
    if (categoryId == 0) {
      categoryId = "";
    }
    console.log(categoryId)
    var that = this;
    // wx.request({
    //   url: 'https://api.it120.cc/' + app.globalData.subDomain + '/shop/goods/list',
    //   data: {
    //     categoryId: categoryId,
    //     nameLike: that.data.searchInput
    //   },
    //   success: function (res) {
    //     that.setData({
    //       goods: [],
    //       loadingMoreHidden: true
    //     });
    //     var goods = [];
    //     if (res.data.code != 0 || res.data.data.length == 0) {
    //       that.setData({
    //         loadingMoreHidden: false,
    //       });
    //       return;
    //     }
    //     for (var i = 0; i < res.data.data.length; i++) {
    //       goods.push(res.data.data[i]);
    //     }
    //     that.setData({
    //       goods: goods,
    //     });
    //   }
    // })

    that.setData({
      goods: [],
      loadingMoreHidden: false
    });

    var goods = [];
    goods.push({ id: "gd001", name: "康师傅", pic: "http://www.jygxs.com/insfile/temp/1afc7899934f432fa655505dc15a2943.jpg", originalPrice: 6.4, stockQty: 10, count:0 });
    goods.push({ id: "gd002", name: "怡宝矿泉水", pic: "http://www.jygxs.com/insfile/temp/1afc7899934f432fa655505dc15a2943.jpg", originalPrice: 6.4, stockQty: 10, count: 0 });
    goods.push({ id: "gd003", name: "燕塘酸奶", pic: "http://www.jygxs.com/insfile/temp/1afc7899934f432fa655505dc15a2943.jpg", originalPrice: 6.4, stockQty: 10, count: 0 });
    goods.push({ id: "gd004", name: "夹心饼干", pic: "http://www.jygxs.com/insfile/temp/1afc7899934f432fa655505dc15a2943.jpg", originalPrice: 6.4, stockQty: 10, count: 0 });
    goods.push({ id: "gd005", name: "巧克力", pic: "http://www.jygxs.com/insfile/temp/1afc7899934f432fa655505dc15a2943.jpg", originalPrice: 6.4, stockQty: 10, count: 0 });
    goods.push({ id: "gd006", name: "黑人洗漱套装", pic: "http://www.jygxs.com/insfile/temp/1afc7899934f432fa655505dc15a2943.jpg", originalPrice: 6.4, stockQty: 10, count: 0 });
    goods.push({ id: "gd007", name: "安全套", pic: "http://www.jygxs.com/insfile/temp/1afc7899934f432fa655505dc15a2943.jpg", originalPrice: 6.4, stockQty: 10, count: 0 });

    if(goods.length>0){
      that.setData({
        goods: goods,
        loadingMoreHidden: true
      });
    }else{
      that.setData({
        goods: goods,
        loadingMoreHidden: false
      });
    }
  },
  tabClick: function (e) {
    this.setData({
      activeCategoryId: e.currentTarget.id
    });
    this.getGoodsList(this.data.activeCategoryId);
  },
  toDetailsTap: function (e) {

    // wx.navigateTo({
    //   url: "/pages/goods-details/index?id=" + e.currentTarget.dataset.id
    // })
  },


  //移除商品
  decreaseCart: function (e) {
    var index = e.currentTarget.dataset.itemIndex;
    if (this.data.goods[index].count<=0){
      return;
    }
    this.data.goods[index].count--
    var num = this.data.goods[index].count;
    var mark = 'a' + index;
    var price = this.data.goods[index].originalPrice;
    var name = this.data.goods[index].name;
    var subPrice = (num * price).toFixed(2)

    var obj = { price: price, num: num, name: name, subPrice: subPrice, index: index, mark: mark };
    var carArray1 = this.data.carArray.filter(item => item.mark != mark);
    if (num > 0){
      carArray1.push(obj);
    }
    
    this.setData({
      carArray: carArray1,
      goods: this.data.goods
    })
    this.calTotalPrice()
    
    //关闭弹起
    if (carArray1.length == 0) {
      this.setData({
        cartShow: 'none'
      })
    }
  },
  //添加到购物车
  addCart(e) {
    var index = e.currentTarget.dataset.itemIndex;
    this.data.goods[index].count++;
    var mark = 'a' + index;
    var price = this.data.goods[index].originalPrice.toFixed(2);
    var num = this.data.goods[index].count;
    var name = this.data.goods[index].name;
    var subPrice = (num * price).toFixed(2)

    var obj = { price: price, num: num, name: name, subPrice: subPrice, index: index, mark: mark };
    var carArray1 = this.data.carArray.filter(item => item.mark != mark)
    carArray1.push(obj)
    this.setData({
      carArray: carArray1,
      goods: this.data.goods
    })
    this.calTotalPrice();
  },

  calTotalPrice: function(){
    var carArray = this.data.carArray;
    var totalPrice = 0;
    var totalCount = 0;
    for (var i = 0; i < carArray.length; i++) {
      totalPrice += carArray[i].price * carArray[i].num;
      totalCount += carArray[i].num
    }
    this.setData({
      totalPrice: totalPrice.toFixed(2),
      totalCount: totalCount
    });
  },

  empty: function(){
    let that = this;
    that.data.goods.forEach(function (e) {
      e.count = 0;
    })  

    wx.showModal({
      title: '提示',
      content: '确认清空购物车？',
      success: function (res) {
        if (res.confirm) {
          that.setData({
            carArray: [],
            totalPrice: 0,
            totalCount: 0,
            cartShow: 'none',
            goods: that.data.goods
          });
        } else if (res.cancel) {
          
        }
      }
    })
  },

  //彈起購物車
  toggleList: function () {
    if (!this.data.totalCount) {
      return;
    }
    this.setData({
      fold: !this.data.fold,
    })
    var fold = this.data.fold
    //console.log(this.data.fold);
    this.cartShow(fold)
  },
  cartShow: function (fold) {
    console.log(fold);
    if (fold == false) {
      this.setData({
        cartShow: 'block',
      })
    } else {
      this.setData({
        cartShow: 'none',
      })
    }
    console.log(this.data.cartShow);
  },

  pay: function(){
    let that = this
    if (this.data.carArray.length > 0){
      debugger
      wx.showModal({
        title: '提示',
        content: '确认提交订单？',
        success: function (res) {
          if (res.confirm) {
            // wx.navigateTo({
            //   url: "/pages/index/index"
            // })
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
  }
})

