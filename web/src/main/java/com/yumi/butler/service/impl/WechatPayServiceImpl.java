package com.yumi.butler.service.impl;

import com.yumi.butler.api.PayMchAPI;
import com.yumi.butler.bean.*;
import com.yumi.butler.common.RequestResult;
import com.yumi.butler.constant.CommonFlag;
import com.yumi.butler.domain.IWechataccountRepository;
import com.yumi.butler.domain.Wechataccount;
import com.yumi.butler.service.WechatPayService;
import com.yumi.butler.utils.DESHelper;
import com.yumi.butler.utils.SignatureUtil;
import com.yumi.butler.utils.YMStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Leo on 2017/8/29.
 */
@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Autowired
    private IWechataccountRepository iWechatAccountRepository;

    //JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
    private String TRADE_TYPE_JSAPI = "JSAPI";
    private String TRADE_TYPE_NATIVE = "NATIVE";
    private String TRADE_TYPE_APP = "APP";


    /**
     * 获取小程序支付所需数据
     * @param platformCode  //WECHAT_XCX_ZHIZHUYUN
     * @param openid
     * @param body
     * @param orderNo
     * @param notifyUrl  //通知连接地址
     * @param attach  //附加数据
     * @param total_fee
     * @return
     */
    public RequestResult getWxAppPayInfo(String platformCode, String openid, String body, String orderNo, String notifyUrl, String attach,
                                         String total_fee, String create_ip, String tradeType) {

        //获取微信账号信息
        RequestResult result1 = getWechatAccount(platformCode);
        if(!result1.isSuccess()){
            return result1;
        }
        Wechataccount wechatAccount = (Wechataccount)result1.getData();
        String appid = wechatAccount.getAppId();
        String mch_id = wechatAccount.getMchId();
        String paternerKey = wechatAccount.getPaternerKey();
        if(StringUtils.isEmpty(appid)||StringUtils.isEmpty(mch_id)||StringUtils.isEmpty(paternerKey)){
            return RequestResult.fail("应用数据错误");
        }
        paternerKey = DESHelper.decrypt(paternerKey, mch_id.concat("wechat_mch").substring(0, 8));

        //获取预订单结果
        RequestResult result = getUnifiedorderResult(platformCode, openid, body, orderNo, notifyUrl, attach, total_fee, create_ip, tradeType);
        if(!result.isSuccess()){
            return result;
        }
        UnifiedorderResult ufR = (UnifiedorderResult)result.getData();

        //预支付交易会话标识，该值有效期为2小时
        if("FAIL".equals(ufR.getReturn_code())){
            return RequestResult.fail(ufR.getReturn_msg().toString());
        }
        String prepay_id = ufR.getPrepay_id();

		//调起支付数据签名
        String timeStamp = System.currentTimeMillis()/1000+"";
        String packageStr = "prepay_id="+prepay_id;
        String signType = "MD5";

        String nonce_str = ufR.getNonce_str();

        Map<String,String> paySignMap = new HashMap<String, String>();
        paySignMap.put("appId", appid);
        paySignMap.put("timeStamp", timeStamp);
        paySignMap.put("nonceStr", nonce_str);
        paySignMap.put("package", packageStr);
        paySignMap.put("signType", signType);
        paySignMap.put("paySign", SignatureUtil.generatePaySign(paySignMap, paternerKey));

        paySignMap.remove("appId");
        return RequestResult.success(paySignMap);
    }

    /**
     * 获取支付二维码链接
     * @param platformCode
     * @param openid
     * @param body
     * @param orderNo
     * @param notifyUrl
     * @param attach
     * @param total_fee
     * @param create_ip
     * @param tradeType
     * @return
     */
    public RequestResult getQRCodeUrl(String platformCode, String openid, String body, String orderNo, String notifyUrl, String attach,
                                      String total_fee, String create_ip, String tradeType){
        RequestResult result = getUnifiedorderResult(platformCode, openid, body, orderNo, notifyUrl, attach, total_fee, create_ip, tradeType);
        if(!result.isSuccess()){
            return result;
        }
        UnifiedorderResult ufR = (UnifiedorderResult)result.getData();
        if("FAIL".equals(ufR.getReturn_code())){
            return RequestResult.fail(ufR.getReturn_msg().toString());
        }
        String code_url = ufR.getCode_url();
        Map<String,String> paySignMap = new HashMap<String, String>();
        paySignMap.put("codeUrl", code_url);
        paySignMap.put("resultCode", ufR.getResult_code());
        paySignMap.put("errCode", ufR.getErr_code());
        paySignMap.put("errCodeDes", ufR.getErr_code_des());
        return RequestResult.success(paySignMap);
    }

    /**
     * 获取订单支付结果
     * @param platformCode
     * @param orderNo
     * @return
     */
    public RequestResult getOrderQuery(String platformCode, String orderNo) {
        //获取微信账号信息
        RequestResult result1 = getWechatAccount(platformCode);
        if(!result1.isSuccess()){
            return result1;
        }
        Wechataccount wechatAccount = (Wechataccount)result1.getData();
        String appid = wechatAccount.getAppId();
        String mch_id = wechatAccount.getMchId();
        String paternerKey = wechatAccount.getPaternerKey();
        if(StringUtils.isEmpty(appid)||StringUtils.isEmpty(mch_id)||StringUtils.isEmpty(paternerKey)){
            return RequestResult.fail("应用数据错误");
        }
        paternerKey = DESHelper.decrypt(paternerKey, mch_id.concat("wechat_mch").substring(0, 8));
        String nonce_str = YMStringUtil.getNonceStr(32);

        /*
		 * 生成签名
		 */
        Map<String,String> map = new HashMap<String,String>();
        map.put("appid", appid);
        map.put("mch_id",mch_id);
        map.put("out_trade_no",orderNo);
        map.put("nonce_str", nonce_str);
        String sign = SignatureUtil.generateSign(map, paternerKey);

		/*
		 * 获取统一支付数据
		 */
        Orderquery oq = new Orderquery();
        oq.setAppid(appid);
        oq.setMch_id(mch_id);
        oq.setNonce_str(nonce_str);
        oq.setOut_trade_no(orderNo);
        oq.setSign(sign);

        PayMchAPI pma = new PayMchAPI();
        OrderqueryResult oqR = pma.getOrderQuery(oq);
        if("FAIL".equals(oqR.getReturn_code())){
            return  RequestResult.fail(oqR.getReturn_msg().toString());
        }
        Map<String,String> resultMap = new HashMap<String, String>();
        resultMap.put("tradeState", oqR.getTrade_state());
        resultMap.put("tradeStateDesc", oqR.getTrade_state_desc());
        resultMap.put("totalFee", oqR.getTotal_fee());
        resultMap.put("transactionId", oqR.getTransaction_id());
        resultMap.put("timeEnd", oqR.getTime_end());

        return RequestResult.success(resultMap);
    }

    /**
     * 统一下单
     * @param platformCode
     * @param openid
     * @param body
     * @param orderNo
     * @param notifyUrl
     * @param attach
     * @param total_fee
     * @param create_ip
     * @param tradeType
     * @return
     */
    public RequestResult getUnifiedorderResult(String platformCode, String openid, String body, String orderNo, String notifyUrl, String attach,
                                               String total_fee, String create_ip, String tradeType){
        total_fee = (Double.parseDouble(total_fee) * 100)+ "";
        total_fee = total_fee.substring(0, total_fee.indexOf("."));
		/*
		 * 获取微信账号信息
		 */
        if(StringUtils.isEmpty(platformCode)){
            return RequestResult.fail("参数错误");
        }

        RequestResult result = getWechatAccount(platformCode);
        if(!result.isSuccess()){
            return result;
        }
        Wechataccount wechatAccount = (Wechataccount)result.getData();
        String appid = wechatAccount.getAppId();
        String mch_id = wechatAccount.getMchId();
        String paternerKey = wechatAccount.getPaternerKey();
        if(StringUtils.isEmpty(appid)||StringUtils.isEmpty(mch_id)||StringUtils.isEmpty(paternerKey)){
            return RequestResult.fail("应用数据错误");
        }
        paternerKey = DESHelper.decrypt(paternerKey, mch_id.concat("wechat_mch").substring(0, 8));

        String nonce_str = YMStringUtil.getNonceStr(32);

		/*
		 * 生成签名
		 */
        Map<String,String> map = new HashMap<String,String>();
        map.put("appid", appid);
        map.put("body",body);
        map.put("attach",attach);
        map.put("mch_id", mch_id);
        map.put("nonce_str", nonce_str);
        map.put("notify_url", notifyUrl);
        map.put("out_trade_no", orderNo);
        map.put("spbill_create_ip", create_ip);
        map.put("total_fee", total_fee);
        map.put("trade_type", tradeType);
        //trade_type=JSAPI时（即公众号支付），此参数必传
        if(TRADE_TYPE_JSAPI.equals(tradeType)){
            map.put("openid", openid);
        }
        String sign = SignatureUtil.generateSign(map, paternerKey);

		/*
		 * 获取统一支付数据
		 */
        Unifiedorder uf = new Unifiedorder();
        uf.setAppid(appid);
        uf.setBody(body);
        uf.setAttach(attach);
        uf.setMch_id(mch_id);
        uf.setNonce_str(nonce_str);
        uf.setNotify_url(notifyUrl);
        uf.setOut_trade_no(orderNo);
        uf.setSpbill_create_ip(create_ip);
        uf.setTotal_fee(total_fee);
        //trade_type=JSAPI时（即公众号支付），此参数必传
        uf.setTrade_type(tradeType);
        if(TRADE_TYPE_JSAPI.equals(tradeType)){
            uf.setOpenid(openid);
        }
        uf.setSign(sign);

        PayMchAPI pma = new PayMchAPI();
        UnifiedorderResult ufR = pma.payUnifiedorder(uf);
        return RequestResult.success(ufR);
    }

    /**
     * 获取微信帐号
     * @param platformCode
     * @return
     */
    public RequestResult getWechatAccount(String platformCode){
        Wechataccount wechatAccount = iWechatAccountRepository.findByPlatformAndFlag(platformCode, CommonFlag.VALID.getValue());
        if(null == wechatAccount){
            return RequestResult.fail("没有找到对应的应用数据");
        }
        return RequestResult.success(wechatAccount);
    }

    /**
     * 申请退款
     * @param platformCode
     * @param orderNo
     * @param out_refund_no
     * @param total_fee
     * @param refund_fee
     * @return
     */
    public RequestResult payRefund(String platformCode, String orderNo, String out_refund_no, String total_fee, String refund_fee){

        total_fee = (Double.parseDouble(total_fee) * 100)+ "";
        total_fee = total_fee.substring(0, total_fee.indexOf("."));
        refund_fee = (Double.parseDouble(refund_fee) * 100)+ "";
        refund_fee = refund_fee.substring(0, refund_fee.indexOf("."));
		/*
		 * 获取微信账号信息
		 */
        if(StringUtils.isEmpty(platformCode)){
            return RequestResult.fail("参数错误");
        }

        RequestResult result = getWechatAccount(platformCode);
        if(!result.isSuccess()){
            return result;
        }
        Wechataccount wechatAccount = (Wechataccount)result.getData();
        String appid = wechatAccount.getAppId();
        String mch_id = wechatAccount.getMchId();
        String paternerKey = wechatAccount.getPaternerKey();
        if(StringUtils.isEmpty(appid)||StringUtils.isEmpty(mch_id)||StringUtils.isEmpty(paternerKey)){
            return RequestResult.fail("应用数据错误");
        }
        paternerKey = DESHelper.decrypt(paternerKey, mch_id.concat("wechat_mch").substring(0, 8));

        String nonce_str = YMStringUtil.getNonceStr(32);
        System.out.println(paternerKey);
        /*
		 * 生成签名
		 */
        Map<String,String> map = new HashMap<String,String>();
        map.put("appid", appid);
        map.put("mch_id", mch_id);
        map.put("nonce_str", nonce_str);
        map.put("out_trade_no", orderNo);
        map.put("out_refund_no", out_refund_no);
        map.put("total_fee", total_fee);
        map.put("refund_fee", refund_fee);
        String sign = SignatureUtil.generateSign(map, paternerKey);

        /**
         * 请求数据
         */
        Refund rf = new Refund();
        rf.setAppid(appid);
        rf.setMch_id(mch_id);
        rf.setNonce_str(nonce_str);
        rf.setOut_trade_no(orderNo);
        rf.setOut_refund_no(out_refund_no);
        rf.setTotal_fee(total_fee);
        rf.setRefund_fee(refund_fee);
        rf.setSign(sign);
        //api
        PayMchAPI pma = new PayMchAPI();
        RefundResult rfr = pma.payRefund(rf);
        return RequestResult.success(rfr);
    }

    /**
     * 查询退款
     * @param platformCode
     * @param orderNo
     * @return
     */
    public RequestResult getRedung(String platformCode, String orderNo) {
        //获取微信账号信息
        RequestResult result1 = getWechatAccount(platformCode);
        if(!result1.isSuccess()){
            return result1;
        }
        Wechataccount wechatAccount = (Wechataccount)result1.getData();
        String appid = wechatAccount.getAppId();
        String mch_id = wechatAccount.getMchId();
        String paternerKey = wechatAccount.getPaternerKey();
        if(StringUtils.isEmpty(appid)||StringUtils.isEmpty(mch_id)||StringUtils.isEmpty(paternerKey)){
            return RequestResult.fail("应用数据错误");
        }
        paternerKey = DESHelper.decrypt(paternerKey, mch_id.concat("wechat_mch").substring(0, 8));
        String nonce_str = YMStringUtil.getNonceStr(32);

        /*
		 * 生成签名
		 */
        Map<String,String> map = new HashMap<String,String>();
        map.put("appid", appid);
        map.put("mch_id",mch_id);
        map.put("out_trade_no",orderNo);
        map.put("nonce_str", nonce_str);
        String sign = SignatureUtil.generateSign(map, paternerKey);

		/*
		 * 获取统一支付数据
		 */
        Orderquery oq = new Orderquery();
        oq.setAppid(appid);
        oq.setMch_id(mch_id);
        oq.setNonce_str(nonce_str);
        oq.setOut_trade_no(orderNo);
        oq.setSign(sign);

        PayMchAPI pma = new PayMchAPI();
        OrderqueryResult oqR = pma.getRefund(oq);
        if("FAIL".equals(oqR.getReturn_code())){
            return  RequestResult.fail(oqR.getReturn_msg().toString());
        }


        return RequestResult.success(oqR.getResult_code());
    }

}
