var app = getApp();
// var BASEAPI = 'http://192.168.10.47:9080';
// var WSAPI = 'ws://192.168.10.47:5544/ws';
// var SERVER = 'http://192.168.10.47:8090';

// var platformAPI = 'http://192.168.10.81:9091';

var platformAPI = 'https://restapi.iyumi.com';
var BASEAPI = 'https://restapi.iyumi.com/';
var WSAPI = 'wss://restapi.iyumi.com/ws';
var SERVER = 'https://api.iyumi.com/web';


function request(url, data, method, callback) {
  wx.request({
    url: url,
    data: data,
    method: method, // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
    header: { 'Content-Type': 'application/json;charset=utf-8' }, // 设置请求的 header
    dataType: 'json',
    success: function (res) {
      // success
      if (callback)
        callback(res.data);
    },
    fail: function (res) {
      // fail
      console.error('request fail : ' + url)
      console.error(res)
      if (callback)
        callback({ success: false, errMsg: res.errMsg });
    },
    complete: function () {

    }
  })
}
function tokenRequest(url, data, method, callback) {
  let _url = url;
  let token = getApp().getWxStorageSync('token');
  // console.log(token)
  wx.request({
    url: url,
    data: data,
    method: method, // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
    header: { "Content-Type": "application/json", "Authorization": "Basic " + token }, // 设置请求的 header
    dataType: 'json',
    success: function (res) {
      //token过期，登录过期
      if (res.statusCode == '401') {
        wx.hideLoading();
        getApp().login();
        // wx.showModal({
        //   title: '提示',
        //   content: '帐号登录太久没有操作，请重新登录!',
        //   showCancel: false,
        //   success: function (res) {
        //     wx.reLaunch({
        //       url: '../login/login',
        //     })
        //   }
        // })
        return;
      }
      if (res.statusCode == '500') {
        wx.hideLoading();
        console.error(`接口${_url}发生错误 :  ${JSON.stringify(res)}`)
        wx.showModal({
          title: '提示',
          content: '服务器发送错误，请稍后重试!',
          showCancel: false,
          success: function (res) { }
        })
        return;
      }
      if ([500, 401, 200].indexOf(res.statusCode) < 0) {
        wx.hideLoading();
        console.error(`接口${_url}发生错误 :  ${JSON.stringify(res)}`)
        wx.showModal({
          title: '提示',
          content: '服务器发送错误，请稍后重试!' + res.statusCode,
          showCancel: false,
          success: function (res) { }
        })
        return;
      }
      // success
      if (callback)
        callback(res.data);
    },
    fail: function (res) {
      // fail
      wx.hideLoading();
      console.error('request fail : ' + url)
      console.error(res)
      if (callback)
        callback({ success: false, errMsg: res.errMsg });
    },
    complete: function () {

    }
  })
}
function uploadRequest(url, data, method, callback) {
  let _url = url;
  wx.request({
    url: url,
    data: data,
    method: method, // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
    header: { "Content-Type": "multipart/form-data"}, // 设置请求的 header
    dataType: 'multipart/form-data',
    success: function (res) {
      //token过期，登录过期
      if (res.statusCode == '401') {
        wx.showModal({
          title: '提示',
          content: '帐号登录太久没有操作，请重新登录!',
          showCancel: false,
          success: function (res) {
            wx.reLaunch({
              url: '../login/login',
            })
          }
        })
        return;
      }
      // success
      if (callback)
        callback(res.data);
    },
    fail: function (res) {
      // fail
      console.error('request fail : ' + url)
      console.error(res)
      if (callback)
        callback({ success: false, errMsg: res.errMsg });
    },
    complete: function () {

    }
  })
}
function requestPay(url, data, method, callback) {
  wx.request({
    url: url,
    data: data,
    method: method, // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
    header: { 'content-type': 'application/x-www-form-urlencoded;charset=utf-8' }, // 设置请求的 header
    dataType: 'json',
    success: function (res) {
      // success
      if (callback)
        callback(res.data);
    },
    fail: function (res) {
      // fail
      console.error('request fail : ' + url)
      console.error(res)
      if (callback)
        callback({ success: false, errMsg: res.errMsg });
    },
    complete: function () {

    }
  })
}
function request1(url, data, method, callback) {
  let _url = url;
  let token = "ZZYBOOKING";
  // console.log(token)
  wx.request({
    url: url,
    data: data,
    method: method, // OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE, CONNECT
    header: { "Content-Type": "application/json", "Authorization": "Basic " + token }, // 设置请求的 header
    dataType: 'json',
    success: function (res) {
      if (callback)
        callback(res.data);
    },
    fail: function (res) {
      // fail
      wx.hideLoading();
      console.error('request fail : ' + url)
      console.error(res)
      if (callback)
        callback({ success: false, errMsg: res.errMsg });
    },
    complete: function () {

    }
  })
}
module.exports = {
  uploadRequest: function (url, data, callback) {
    uploadRequest(url, data, 'POST', callback);
  },
  get: function (url, data, callback) {
    request(url, data, 'GET', callback);
  },
  post: function (url, data, callback) {
    request(url, data, 'POST', callback);
  },
  put: function (url, data, callback) {
    request(url, data, 'PUT', callback);
  },
  delete: function (url, data, callback) {
    request(url, data, 'DELETE', callback);
  },
  postpay: function (url, data, callback) {
    requestPay(url, data, 'POST', callback);
  },
  getpf: function (url, data, callback) {
    request1(url, data, 'GET', callback);
  },
  postpf: function (url, data, callback) {
    request1(url, data, 'POST', callback);
  },
  putpf: function (url, data, callback) {
    request1(url, data, 'PUT', callback);
  },
  BASE_URL: BASEAPI,
  WEB_URL: BASEAPI + '/yumi_bee/',
  API_URL: BASEAPI + '/yumi_bee/api/wx/',
  WEBSOCKET_URL: WSAPI,
  YUMIHUI_URL: SERVER,
  PLAR_URL: platformAPI,
}