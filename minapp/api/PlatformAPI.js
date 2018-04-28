var Request = require('../utils/RequestUtil.js');

var SERVER = Request.BASE_URL;
var plarform = Request.PLAR_URL;

module.exports = {
  getRoomList: function (data, callback) {
    Request.getpf(plarform + '/platformservice/room/bookingRoomList', data, callback);
  },
  getOrderList: function (data, callback) {//C端预订列表
    Request.getpf(plarform + `/platformservice/order/butlerOrderList`, data, callback)
  },
  getRoomPrice: function (data, callback) {
    Request.getpf(plarform + `/platformservice/saleprice`, data, callback)
  },
  addOrder: function (data, callback) {//C端预订
    Request.postpf(plarform + `/platformservice/order`, data, callback)
  },
  getOrderDetail: function (data, callback) {//获取订单详情
    Request.getpf(plarform + `/platformservice/order/${data['orderNo']}`, data, callback)
  },
  getLockpwd: function (data, callback) {//获取门锁密码
    Request.getpf(plarform + `/platformservice/lockpwd/send`, data, callback)
  },
  getDynamicPwd: function (data, callback) {//获取门锁密码
    Request.getpf(plarform + `/platformservice/lockpwd/getDynamicPwd`, data, callback)
  },
  updOrderStatus: function (data, callback) {//修改订单状态
    Request.putpf(plarform + `/platformservice/order/updOrderStatus`, data, callback);
  },
  updOrderGuest: function (data, callback) {//修改入住人
    Request.putpf(plarform + `/platformservice/order/updOrderGuest`, data, callback);
  },
  getPayInfo: function (data, callback) {//获取支付结果
    Request.getpf(plarform + `/platformservice/order/getPayInfo`, data, callback)
  },

  uploadFace: SERVER + '/yumi_bee/platform/uploadFace',
  uploadIDCard: SERVER + '/yumi_bee/platform/uploadIDCard',

}