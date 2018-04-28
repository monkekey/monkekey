// pages/roomChange/roomChange.js
const app = getApp();
const platformAPI = require('../../api/PlatformAPI.js');
const Moment = require('../../utils/Moment.js')

var SERVICE_TAGS = {
  'KTV': 'ktv.png',
  '餐厅': 'breakfast.png',
  '茶水室': 'tea.png',
  '吹风机': 'blower.png',
  '电热水壶': 'kettle.png',
  '电梯': 'elevator.png',
  '国际长途电话': 'toll_call.png',
  '国内长途电话': 'international_call.png',
  '会议室': 'meeting.png',
  '健身房': 'running.png',
  '接待外宾': 'receive.png',
  '接机服务': 'jieji.png',
  '咖啡厅': 'coffe.png',
  '旅游票务': 'coffe.png',
  '无线WIFI': 'wifi.png',
  '宽带': 'broadband.png',
  '免费停车场': 'parking.png',
  '收费停车场': 'parking.png',
  '棋牌室': 'chess.png',
  '商务中心': 'cbd.png',
  '拖鞋': 'slipper.png',
  '外币兑换': 'currency.png',
  '游泳池': 'swiming.png',
  '早餐服务': 'fastfood.png',
  '洗衣服务': 'washer.png',
  '空调': 'air_conditioner.png',
  '房源类型': 'icon_home.png',
  '床铺': 'icon_bed.png',
  '可住人数': 'icon_guest.png',
  '浴室': 'icon_bathtub.png',
  '面积': 'icon_service.png',
  '卧室': 'icon_room.png',
  '厨房': 'kitchen.png',

  '热水淋浴': 'bath.png',
  '浴缸': 'bathtub.png',
  '手纸': 'paper.png',
  '牙具': 'tooth.png',
  '毛巾': 'maoj.png',
  '浴液、洗发水': 'soapoo.png',
  '香皂': 'soap.png',
  '洗衣机': 'washer.png',
  '电视': 'tv.png',
  '冰箱': 'icebox.png',
  '饮水设备': 'kettle.png',
  '门禁系统': 'lock.png',
  '暖气': 'heater.png',
  '允许做饭': 'cooker.png',
  '允许吸烟': 'smoke.png',
  '允许聚会': 'ktv.png',
  '允许带宠物': 'pet.png',
};

const roomChangeData = {
  data: {
    globalData:app.globalData,
    userInfo: '',
    roomlist:[],
    roomInfo:'',
    queryOrder: false,
    orderlist:[],
    orderInfo:'',
    addguest: false,
    manNum:1,
    loading:false,
    roomPage:{
      pageIdx: 0,
      pageSize: 10,
      pageEnd: false,
    },
    orderPage:{
      pageIdx: 0,
      pageSize: 30,
      pageEnd: false,
    },
    keyword:'',
    linkphone:'',
  },

  onLoad: function (options) {
    wx.setStorageSync("beginDay", "")
    wx.setStorageSync("endDay", "")

  },

  onShow: function () {
    var userInfo = wx.getStorageSync('LavandeUserInfo');
    if (!userInfo){
      getApp().getUserInfo();
      userInfo = getApp().globalData.userInfo;
    }
    let beginTimeText = wx.getStorageSync("beginDay")
    let endTimeText = wx.getStorageSync("endDay")
    if (beginTimeText && endTimeText){
      var datearr1 = beginTimeText.split("/")
      var datearr2 = endTimeText.split("/")
      beginTimeText = datearr1[1] + '/' + datearr1[2];
      endTimeText = datearr2[1] + '/' + datearr2[2];
    }
    this.setData({ userInfo, beginTimeText, endTimeText})
    this.initData();
  },
  //获取房源
  initData: function(){
    let i=0;
    let roomlist = [];
    let that = this;
    let roomPage = this.data.roomPage;
    if (roomPage.pageEnd) return;
    var now = new Date();
    let beginTime = wx.getStorageSync("beginDay");
    let endTime = wx.getStorageSync("endDay");
    beginTime = beginTime ? beginTime.replace(/\//g, '-') : Moment(now).format('yyyy-MM-dd').replace(/\//g, '-');
    endTime = endTime ? endTime.replace(/\//g, '-') : Moment(now).add(1,'day').format('yyyy-MM-dd').replace(/\//g, '-');
    let formdata = {
      pageSize: roomPage.pageSize,
      pageIdx: roomPage.pageIdx,
      beginTime: beginTime,
      endTime: endTime,
      lockroom: 'lockroom'
    }
    if (this.data.loading){return;}
    this.setData({loading:true})
    platformAPI.getRoomList( formdata, (res) => {
      that.setData({ loading: false });
      var data = [];
      if (res.success) {
        data = res.data.roomList;
        let propertys = res.data.propertys;
        let types = res.data.types;
        for (var d in data) {
          //房源图片
          if (data[d].mainImage == null && data[d].imgs.length > 0) {
            data[d].mainImage = data[d].imgs[0].viewPath;
          }
          //房价
          var price = 0;
          data[d].property = [];
          for (var i in data[d].props) {
            if (now.getDay() == 5 || now.getDay() == 6) {
              if (data[d].props[i].propertyCode == 'WeekendSalePrice') {
                price = data[d].props[i].propertyValue;
                data[d].WeekendSalePrice = data[d].props[i].propertyValue;
                continue;
              }
            }else{
              if (data[d].props[i].propertyCode == 'BasicSalePrice') {
                price = data[d].props[i].propertyValue;
                data[d].BasicSalePrice = data[d].props[i].propertyValue;
                continue;
              }
            }
            if (data[d].props[i].propertyCode == 'RoomType') {
              data[d].roomTypeCode = data[d].props[i].propertyValue;
              continue;
            }
            if (data[d].props[i].propertyCode == 'RoomType') {
              data[d].roomTypeCode = data[d].props[i].propertyValue;
              continue;
            }
            if (data[d].props[i].propertyCode == 'Area') {
              data[d].area = data[d].props[i].propertyValue;
              continue;
            }
            for (var p in propertys){
              if (propertys[p].parentId == 5 && data[d].props[i].propertyCode == propertys[p].roomPropertyCode){
                if (data[d].props[i].propertyValue && data[d].props[i].propertyValue != 0){
                  data[d].props[i].propertyName = propertys[p].roomPropertyName
                  data[d].props[i].url = SERVICE_TAGS[propertys[p].roomPropertyName];
                  data[d].property.push(data[d].props[i])
                  break;
                }
              }
            }
            
          }
          for (var j in types){
            if (data[d].roomTypeCode == types[j].roomTypeShortCode){
              data[d].roomTypeName = types[j].roomTypeName
              break;
            }
          }
          if (!data[d].roomTypeName){
            data[d].roomTypeName = data[d].roomName;
          }
          
          data[d].price = data[d].prices.length > 0 ? data[d].prices[0].salePrice : price;
          data[d].select = false;
        }
        let roomlist = that.data.roomlist || [];
        roomlist.push(...data);
        if (data.length < roomPage.pageSize) {
          roomPage.pageEnd = true;
        }
        ++roomPage.pageIdx;
        that.setData({
          roomlist,
          roomPage,
          queryOrder: false,
        });
      }
    })
  },
  upper:function(){
    console.log('滚到最上方')
  },
  lower:function(){
    console.log('滚到最下方')
    if (this.data.queryOrder){
      // this.getPlatOrder();
    }else{
      this.initData();
    }
  },
  //停止当前页面下拉刷新
  onPullDownRefresh: function () {
    if (this.data.queryOrder){

    }else{
      this.setData({
        roomPage: {
          pageIdx: 0,
          pageSize: 10,
          pageEnd: false,
        },
        roomlist: [],
        roomInfo: '',
        orderPage: {
          pageIdx: 0,
          pageSize: 30,
          pageEnd: false,
        },
        orderlist: [],
        orderInfo: '',
      })
      this.initData();
    }
  },
  //选中房源
  roomSel:function(e){
    // let index = e.currentTarget.dataset.index;
    let item = e.currentTarget.dataset.item;
    let roomlist = this.data.roomlist;
    let roomInfo = '';

    for (var i in roomlist){
      if (item.roomNo == roomlist[i].roomNo){
        roomlist[i].select = !item.select;
        if (roomlist[i].select){
          roomInfo = roomlist[i];
        }
      }else{
        roomlist[i].select = false;
      }
    }

    let orderlist = this.data.orderlist;
    let orderInfo = '';
    for (var i in orderlist) {
      orderlist[i].select = false;
    }
    this.setData({ roomlist, roomInfo, orderlist, orderInfo})
  },
  //日历
  onClaner:function(e){
    wx.navigateTo({
      url: '../claner/claner',
    })
  },
  //增加房客
  addMan(){
    let addguest = this.data.addguest;
    this.setData({
      addguest: !addguest
    })
  },
  //增加房客
  addNum(e){
    let manNum = this.data.manNum;
    let item = e.currentTarget.dataset.item;
    if (item == 'add'){
      if (manNum == 10){
        return;
      }
      manNum++;
    }else{
      if (manNum == 1){
        return;
      }
      manNum--;
    }
    this.setData({ manNum})
  },
  //获取订单
  getOrder(){
    let queryOrder = true;
    this.setData({ queryOrder})
    this.search()
  },
  onPut(e){
    let keyword = e.detail.value;
    this.setData({ keyword})
  },
  search(){
    // var regex = new RegExp('^[1][0-9]{10}$', 'g');
    // if (!regex.exec(this.data.keyword)){
    //   wx.showModal({
    //     title: '提示',
    //     content: '请输入正确手机号',
    //   })
    //   return;
    // }
    let roomlist = this.data.roomlist;
    let roomInfo = '';
    for (var r in roomlist) {
      roomlist[r].select = false;
    }
    let orderPage = {
      pageIdx: 0,
      pageSize: 30,
      pageEnd: false,
    };
    let orderlist = [];
    this.setData({
      orderlist, orderInfo: '', orderPage,
      roomlist, roomInfo,
    })
    this.getPlatOrder();
  },
  //清除订单
  cleanOrder(e) {
    let queryOrder = false;
    let orderlist = this.data.orderlist;
    for (var i in orderlist) {
      orderlist[i].select = false;
    }

    let roomlist = this.data.roomlist;
    let roomInfo = '';
    for (var r in roomlist) {
      roomlist[r].select = false;
    }
    let orderPage = {
      pageIdx: 0,
      pageSize: 30,
      pageEnd: false,
    };
    this.setData({
      orderlist, orderInfo: '', orderPage,
      roomlist, roomInfo, queryOrder,
    })
  },
  getPlatOrder(){
    //获取智猪云客户订单列表
    var that = this;
    let pageEnd = this.data.orderPage.pageEnd;
    // if (pageEnd) return;
    if (this.data.loading) return;

    let orderInfo = this.data.orderInfo;
    let pageIdx = that.data.orderPage.pageIdx;
    let pageSize = that.data.orderPage.pageSize;
    let beginTime = wx.getStorageSync("beginDay")
    let endTime = wx.getStorageSync("endDay")
    if (!beginTime || !endTime) {
      beginTime = Moment(new Date()).format('yyyy/MM/dd');
      endTime = Moment(new Date()).add(1, 'day').format('yyyy/MM/dd');
    }
    let beginDate = beginTime.replace(/\//g, '-');
    let endDate = endTime.replace(/\//g, '-');
    var param = {
      pageIdx: pageIdx,
      pageSize: pageSize,
      linkphone: this.data.keyword,
      linkman: this.data.userInfo.nickName,
      orderStatus:'C',
      beginDate: beginDate,
      endDate: endDate,
    };
    that.setData({ loading: true });
    platformAPI.getOrderList(param, function (res) {
      that.setData({ loading: false });
      if (!res.success) {
        wx.showModal({
          title: '提示',
          content: '获取订单失败！' + res.errMsg,
          showCancel: false
        })
        return;
      }
      var data = res.data;
      var orderData = [];//that.data.orderlist;
      if (data.length < pageSize) pageEnd = true;
      for (var i = 0; i < data.length; i++) {
        var d = data[i];
        d.remark = JSON.parse(d.remark);
        d.ShortCreateTimeInUtc = d.createTime.split(' ')[0].split('-')[1] + '月' + d.createTime.split(' ')[0].split('-')[2] + '日';
        d.estimateBeginTime = d.estimateBeginTime.substring(0, d.estimateBeginTime.length - 3);
        d.estimateEndTime = d.estimateEndTime.substring(0, d.estimateEndTime.length - 3);
        d.select = false;
        if (orderInfo.orderNo == d.orderNo) {
          d.select = true;
        }
        orderData.push(d);
      }
      ++pageIdx;
      that.setData({
        queryOrder: true,
        orderPage:{
          pageEnd: pageEnd,
          pageIdx: pageIdx
        },
        orderlist: orderData,
      })
    })
  },
  //选中订单
  orderSel(e){
    // let index = e.currentTarget.dataset.index;
    let item = e.currentTarget.dataset.item;
    let orderlist = this.data.orderlist;
    let orderInfo = '';

    let roomlist = this.data.roomlist;
    let roomInfo = '';

    for (var i in orderlist) {
      if (item.orderNo == orderlist[i].orderNo) {
        orderlist[i].select = !item.select;
      } else {
        orderlist[i].select = false;
      }
      if (orderlist[i].select) {
        // orderInfo.push(orderlist[i]);
        orderInfo = orderlist[i];
        if (orderInfo.roomCode){
          for (var r in roomlist) {
            if (roomlist[r].roomCode == orderInfo.roomCode) {
              roomlist[r].select = true;
              roomInfo = roomlist[r];
            }else{
              roomlist[r].select = false;
            }
          }
        }
      }
    }
    this.setData({ orderlist, orderInfo, roomlist, roomInfo })
  },
  //获取手机权限
  getPhoneNumber: function (e) {

  },
  //确定
  onBill(){
    let info = '';
    if (this.data.queryOrder){
      this.setData({ queryOrder:false})
    }else{
      if (!this.data.roomInfo) {
        info = '请选择房间';
        wx.showModal({
          title: '提示',
          content: info,
        })
        return;
      }

      let beginTime = wx.getStorageSync("beginDay")
      let endTime = wx.getStorageSync("endDay")
      if (!beginTime || !endTime) {
        beginTime = Moment(new Date()).format('yyyy/MM/dd');
        endTime = Moment(new Date()).add(1, 'day').format('yyyy/MM/dd');
      }
      wx.setStorageSync("roomInfo", this.data.roomInfo);
      if (this.data.orderInfo) {
        let orderInfo = this.data.orderInfo;
        beginTime = Moment(orderInfo.estimateBeginTime).format('yyyy/MM/dd');
        endTime = Moment(orderInfo.estimateEndTime).format('yyyy/MM/dd');
      }
      this.getRoomPrice(beginTime, endTime);
    }
  },
  //获取房价
  getRoomPrice: function (beginTime, endTime) {
    var that = this;
    var checkInDate = beginTime.replace(/\//g, '-')
    var checkOutDate = endTime.replace(/\//g, '-')
    var params = {
      roomCode: this.data.roomInfo.roomCode,
      beginDt: checkInDate,
      endDt: checkOutDate
    }
    wx.showLoading({})
    platformAPI.getRoomPrice(params, (res) => {
      if (!res.success) {
        wx.showModal({
          title: '提示',
          content: res.errMsg,
          showCancel: false,
          confirmText: '重新下单',
          success: function () {
            _this.getRoomPrice();
          }
        })
        return;
      }
      var amount = 0;
      var diff = Moment(checkOutDate.replace(/\-/g, '/')).differ(checkInDate.replace(/\-/g, '/'));
      var roomData = that.data.roomInfo;

      var orderPrice = { checkInDate: checkInDate, checkOutDate: checkOutDate }
      orderPrice.dayPrice = [];
      for (var i = 0; i < diff; i++) {
        var dayPrice = {};
        var priDate = Moment(checkInDate).add(i, 'day').format('YYYY-MM-DD').replace(/\//g, '-');
        var dateTime = new Date(priDate);
        var basePrice = 0;
        if (dateTime.getDay() == 5 || dateTime.getDay() == 6) {
          basePrice = roomData.WeekendSalePrice;//周末价
        } else {
          basePrice = roomData.BasicSalePrice;//平日价
        }
        dayPrice[priDate] = res.data[priDate] || basePrice; //售价
        amount += parseFloat(dayPrice[priDate]);
        orderPrice.dayPrice.push(dayPrice)
      }
      wx.setStorageSync('orderPrice', orderPrice);
      wx.setStorageSync("beginDay", checkInDate)
      wx.setStorageSync("endDay", checkOutDate);
      if (that.data.orderInfo) {
        that.data.orderInfo.amount = amount
        if (amount != that.data.orderInfo.amount){
          wx.showModal({
            title: '提示',
            content: '订单价格错误，请重新下单',
          })
          return;
        }
        wx.hideLoading()
        wx.setStorageSync("orderInfo", that.data.orderInfo);
        // if (that.data.orderInfo.status == 'I'){
        //   wx.navigateTo({
        //     url: '../checkDetail/checkDetail',
        //   })
        // }else{
          wx.navigateTo({
            url: '../cheakin/cheakin',
          })
        // }
      }else{
        if (that.data.linkphone){
          // this.data.linkphone = '13554729361';//手机号
        }
        that.addOrder(amount, orderPrice, beginTime, endTime)
      }
    })

  },
  addOrder(amount, orderPrice, beginTime, endTime){
    let that = this;
    let dayPrices = orderPrice.dayPrice;
    let tranList = []
    for (var i = 0; i < dayPrices.length; i++) {
      for (var key in dayPrices[i]) {
        tranList.push({ credit: dayPrices[i][key], accDate: key, accItemCode: '1010' })
      }
    }
    var orderGuestList = [];
    var guestInfo = this.data.userInfo;
    var guest = { guestName: guestInfo.nickName, guestMobile: this.data.linkphone };
    orderGuestList.push(guest);
    let params = {
      accountCode: 'AR_wx',//AR账
      amount: amount,//总房费
      channelCode: 'individualGuests',//渠道--默认散客
      estimateBeginTime: Moment(beginTime.replace(/\-/g, '/') + ' 14:00:00').format('yyyy-MM-dd hh:mm:ss'),//入住时间
      estimateEndTime: Moment(endTime.replace(/\-/g, '/') + ' 12:00:00').format('yyyy-MM-dd hh:mm:ss'),//退房时间
      remark: '',//备注
      tranList: tranList,//每日房费
      guestNum: this.data.manNum,//入住人数
      orderGuestList: orderGuestList,//入住人
      orderType: 'SK',//订单类型,
      linkman: guestInfo.nickName,
      linkphone: this.data.linkphone,
      roomCode: this.data.roomInfo.roomCode//房间号'SZ150011016508036068'
    }

    platformAPI.addOrder( params, function(res){
      wx.hideLoading()

      if(!res.success){
        wx.showModal({
          title: '提示',
          content: res.errMsg,
          showCancel: false
        })
        return;
      }
      wx.setStorageSync("orderInfo", res.data);
      wx.navigateTo({
        url: '../cheakin/cheakin',
      })
    })
    
  },
  getZhiZhuYunOrderStatus: function (status) {
    var statusText = '';
    switch (status) {
      case 'W': statusText = '待支付'; break;
      case 'X': statusText = '已支付'; break;
      case 'N': statusText = '待接单'; break;
      case 'P': statusText = '待入住'; break;
      case 'C': statusText = '已取消'; break;
      case 'I': statusText = '已入住'; break;
      case 'T': statusText = '已退房'; break;
      default: break;
    }
    return statusText;
  },
  onProp(e){
    console.log(e)
    let item = e.currentTarget.dataset.item;
    let roomProp = item.property;
    wx.navigateTo({
      url: '../roomProp/roomProp?roomProp=' + JSON.stringify(roomProp),
    })
  },
}
Page(Object.assign(roomChangeData,{}))