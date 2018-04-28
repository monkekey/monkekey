package com.yumi.butler.service;

import com.yumi.butler.common.RequestResult;

/**
 * Created by Leo on 2017/8/29.
 */
public interface WechatPayService {

    public RequestResult getWxAppPayInfo(String platformCode, String openid, String body, String orderNo, String notifyUrl, String attach,
                                         String total_fee, String create_ip, String tradeType);

    public RequestResult getQRCodeUrl(String platformCode, String openid, String body, String orderNo, String notifyUrl, String attach,
                                      String total_fee, String create_ip, String tradeType);

    public RequestResult getOrderQuery(String platformCode, String orderNo);

    public RequestResult payRefund(String platformCode, String orderNo, String out_refund_no, String total_fee, String refund_fee);

    public RequestResult getRedung(String platformCode, String orderNo);

}
