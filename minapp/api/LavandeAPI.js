//后台接口
var Request = require('../utils/RequestUtil.js');

var SERVER = Request.API_URL;

module.exports = {
  getOpenId: function (data, callback) {
    Request.get(SERVER + `/getOpenId`, data, callback);
  },
  getButlerInfo: function (data, callback) {
    Request.get(SERVER + `/getButlerInfo`, data, callback);
  },
  getNearestInn: function (data, callback) {
    Request.get(SERVER + `/getNearstInn`, data, callback);
  },
  postStar: function (data, callback) {
    Request.post(SERVER + `/star`, data, callback);
  },
  postAdmireInfo: function(data,callback){
    Request.post(SERVER + `saveAdmireInfo`, data, callback)
  },
  getCommentTagAll: function(data,callback){
    Request.get(SERVER + `/getTagAll`, data, callback)
  },
  postComment: function (data, callback){
    Request.post(SERVER + `saveComment`, data, callback)
  },
  getWiFi: function (data, callback) {
    Request.get(SERVER + `getWiFi`, data, callback)
  },
  getCount: function (data, callback){
    Request.get(SERVER + `getCount`, data, callback)
  },
  getCommetCount: function (data, callback) {
    Request.get(SERVER + `getCommetCount`, data, callback)
  },


  getCategory:function(data,callback){
    Request.get(SERVER + `getCategory`, data, callback)
  },
  getHotCommodity: function (data, callback) {
    Request.get(SERVER + `getHotCommodity`, data, callback)
  },
  getNewCommodity: function (data, callback) {
    Request.get(SERVER + `getNewCommodity`, data, callback)
  },
  getLikeCommodity: function (data, callback) {
    Request.get(SERVER + `getLikeCommodity`, data, callback)
  },
  getCommodity: function (data, callback) {
    Request.get(SERVER + `getCommodity`, data, callback)
  },
  savePayment: function (data, callback) {
    Request.post(SERVER + `savePayment`, data, callback)
  },
  getOrderListWx: function (data, callback) {
    Request.get(SERVER + `getOrderListWx`, data, callback)
  },
  getOrderInfoWx: function (data, callback) {
    Request.get(SERVER + `getOrderInfoWx`, data, callback)
  },
  getOrderStatusWx: function (data, callback) {
    Request.get(SERVER + `OrderStatusWx`, data, callback)
  },
  getKeywordCommodity: function (data, callback) {
    Request.get(SERVER + `getKeywordCommodity`, data, callback)
  },
  checkStorage: function (data, callback) {
    Request.post(SERVER + `checkStorage`, data, callback)
  },
  cleanOrder: function (data, callback) {
    Request.post(SERVER + `cleanOrder`, data, callback)
  }
}