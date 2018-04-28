package com.yumi.butler.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 申请退款请求返回对象
 */

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class RefundResult extends MchBaseResult{

	@XmlElement
	private String device_info;

	@XmlElement
	private String transaction_id;

	@XmlElement
	private String out_trade_no;

	@XmlElement
	private String out_refund_no;

	@XmlElement
	private String refund_id;

	@XmlElement
	private String refund_channel;

	@XmlElement
	private Integer refund_fee;

	@XmlElement
	private Integer coupon_refund_fee;

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getRefund_channel() {
		return refund_channel;
	}

	public void setRefund_channel(String refund_channel) {
		this.refund_channel = refund_channel;
	}

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}

	public Integer getCoupon_refund_fee() {
		return coupon_refund_fee;
	}

	public void setCoupon_refund_fee(Integer coupon_refund_fee) {
		this.coupon_refund_fee = coupon_refund_fee;
	}

//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String result_code;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String err_code;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String err_code_des;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String appid;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String mch_id;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String nonce_str;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String sign;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String transaction_id;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String out_trade_no;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String out_refund_no;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String refund_id;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String refund_fee;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String settlement_refund_fee;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String total_fee;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String settlement_total_fee;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String fee_type;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String cash_fee;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String cash_fee_type;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String cash_refund_fee;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String coupon_type_$n;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String coupon_refund_fee;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String coupon_refund_fee_$n;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String coupon_refund_count;
//
//	@XmlElement
//	@XmlJavaTypeAdapter(value = AdaptorCDATA.class)
//	private String coupon_refund_id_$n;
//
//
//	@Override
//	public String getResult_code() {
//		return result_code;
//	}
//
//	@Override
//	public void setResult_code(String result_code) {
//		this.result_code = result_code;
//	}
//
//	@Override
//	public String getErr_code() {
//		return err_code;
//	}
//
//	@Override
//	public void setErr_code(String err_code) {
//		this.err_code = err_code;
//	}
//
//	@Override
//	public String getErr_code_des() {
//		return err_code_des;
//	}
//
//	@Override
//	public void setErr_code_des(String err_code_des) {
//		this.err_code_des = err_code_des;
//	}
//
//	@Override
//	public String getAppid() {
//		return appid;
//	}
//
//	@Override
//	public void setAppid(String appid) {
//		this.appid = appid;
//	}
//
//	@Override
//	public String getMch_id() {
//		return mch_id;
//	}
//
//	@Override
//	public void setMch_id(String mch_id) {
//		this.mch_id = mch_id;
//	}
//
//	@Override
//	public String getNonce_str() {
//		return nonce_str;
//	}
//
//	@Override
//	public void setNonce_str(String nonce_str) {
//		this.nonce_str = nonce_str;
//	}
//
//	@Override
//	public String getSign() {
//		return sign;
//	}
//
//	@Override
//	public void setSign(String sign) {
//		this.sign = sign;
//	}
//
//	public String getTransaction_id() {
//		return transaction_id;
//	}
//
//	public void setTransaction_id(String transaction_id) {
//		this.transaction_id = transaction_id;
//	}
//
//	public String getOut_trade_no() {
//		return out_trade_no;
//	}
//
//	public void setOut_trade_no(String out_trade_no) {
//		this.out_trade_no = out_trade_no;
//	}
//
//	public String getOut_refund_no() {
//		return out_refund_no;
//	}
//
//	public void setOut_refund_no(String out_refund_no) {
//		this.out_refund_no = out_refund_no;
//	}
//
//	public String getRefund_id() {
//		return refund_id;
//	}
//
//	public void setRefund_id(String refund_id) {
//		this.refund_id = refund_id;
//	}
//
//	public String getRefund_fee() {
//		return refund_fee;
//	}
//
//	public void setRefund_fee(String refund_fee) {
//		this.refund_fee = refund_fee;
//	}
//
//	public String getSettlement_refund_fee() {
//		return settlement_refund_fee;
//	}
//
//	public void setSettlement_refund_fee(String settlement_refund_fee) {
//		this.settlement_refund_fee = settlement_refund_fee;
//	}
//
//	public String getTotal_fee() {
//		return total_fee;
//	}
//
//	public void setTotal_fee(String total_fee) {
//		this.total_fee = total_fee;
//	}
//
//	public String getSettlement_total_fee() {
//		return settlement_total_fee;
//	}
//
//	public void setSettlement_total_fee(String settlement_total_fee) {
//		this.settlement_total_fee = settlement_total_fee;
//	}
//
//	public String getFee_type() {
//		return fee_type;
//	}
//
//	public void setFee_type(String fee_type) {
//		this.fee_type = fee_type;
//	}
//
//	public String getCash_fee() {
//		return cash_fee;
//	}
//
//	public void setCash_fee(String cash_fee) {
//		this.cash_fee = cash_fee;
//	}
//
//	public String getCash_fee_type() {
//		return cash_fee_type;
//	}
//
//	public void setCash_fee_type(String cash_fee_type) {
//		this.cash_fee_type = cash_fee_type;
//	}
//
//	public String getCash_refund_fee() {
//		return cash_refund_fee;
//	}
//
//	public void setCash_refund_fee(String cash_refund_fee) {
//		this.cash_refund_fee = cash_refund_fee;
//	}
//
//	public String getCoupon_type_$n() {
//		return coupon_type_$n;
//	}
//
//	public void setCoupon_type_$n(String coupon_type_$n) {
//		this.coupon_type_$n = coupon_type_$n;
//	}
//
//	public String getCoupon_refund_fee() {
//		return coupon_refund_fee;
//	}
//
//	public void setCoupon_refund_fee(String coupon_refund_fee) {
//		this.coupon_refund_fee = coupon_refund_fee;
//	}
//
//	public String getCoupon_refund_fee_$n() {
//		return coupon_refund_fee_$n;
//	}
//
//	public void setCoupon_refund_fee_$n(String coupon_refund_fee_$n) {
//		this.coupon_refund_fee_$n = coupon_refund_fee_$n;
//	}
//
//	public String getCoupon_refund_count() {
//		return coupon_refund_count;
//	}
//
//	public void setCoupon_refund_count(String coupon_refund_count) {
//		this.coupon_refund_count = coupon_refund_count;
//	}
//
//	public String getCoupon_refund_id_$n() {
//		return coupon_refund_id_$n;
//	}
//
//	public void setCoupon_refund_id_$n(String coupon_refund_id_$n) {
//		this.coupon_refund_id_$n = coupon_refund_id_$n;
//	}
}
