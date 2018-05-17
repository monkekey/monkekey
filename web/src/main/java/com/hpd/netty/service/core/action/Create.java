package com.hpd.netty.service.core.action;

import com.hpd.netty.model.JSONtype;
import com.hpd.netty.model.SysCode;
import com.alibaba.fastjson.JSONObject;


/**
 * 创建连接
 * 
 * @author tdz
 * @date 2016年12月12日
 *
 */
public final class Create {

	public static Object execute() {
		JSONObject json = new JSONObject();
		json.put("type", JSONtype.CREATE);
		json.put("result", SysCode.success);
		return json.toString();
	}
}
