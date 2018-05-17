package com.hpd.netty.service;

import com.hpd.netty.model.SysCode;
import com.hpd.netty.tools.ServerLog;
import com.alibaba.fastjson.JSONObject;
import com.hpd.netty.model.JSONtype;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import com.hpd.netty.model.Type;
import com.hpd.netty.service.core.action.Join;
import com.hpd.netty.service.core.action.Msg;


/**
 * 不同消息类型处理及转发
 * 
 * @author tdz
 * @date 2016年12月12日
 *
 */
public final class MsgExecute {

	private TextWebSocketFrame msg;// webSocket消息体
	private ChannelHandlerContext ctx;
	private ChannelGroup group;

	public MsgExecute(ChannelGroup group, TextWebSocketFrame msg, ChannelHandlerContext ctx) {
		this.msg = msg;
		this.ctx = ctx;
		this.group = group;
	}

	public JSONObject execute() {
		JSONObject json = new JSONObject();

		if (msg == null || msg.text() == null) {
			json.put("result", ServerLog.print(Type.ERROR, SysCode.msg_typeError));
		} else {
			try {
				JSONObject jsonType = JSONObject.parseObject(msg.text());
				String type = jsonType.getString("type");
				switch (type) {
					// 用户进入
					case JSONtype.JOIN:
						new Join(group, msg, ctx).execute(json, type);
						break;
					// 点对点聊天
					case JSONtype.MSGTO:
						new Msg(group, msg, ctx).sendTo();
						break;
				}
			}
//			catch (JSONException e) {
//				json.put("result", ServerLog.print(Type.ERROR, e, SysCode.msg_typeError));
//			}
			catch (Exception e) {
				json.put("result", ServerLog.print(Type.ERROR, e, SysCode.sys_unknownException));
			}
		}
		return json;
	}

}
