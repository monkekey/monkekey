var Request = require('../utils/RequestUtil.js');

var SERVER = Request.YUMIHUI_URL;

module.exports={
    getOpenid:function(data,callback){
        Request.postpay(SERVER+'/booking/wechat/v1/getXcxOpenid',data,callback);
    },
    actPayData:function(data,callback){
        Request.postpay(SERVER+'/booking/wechat/v1/actPayData',data,callback);
    }
}