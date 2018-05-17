package com.hpd.netty.service.core.action;

import com.hpd.butler.domain.Msginfo;
import com.hpd.netty.model.Data;
import com.hpd.netty.model.SysCode;
import com.hpd.netty.tools.ServerLog;
import com.alibaba.fastjson.JSONObject;
import com.hpd.netty.service.core.DbService;
import com.hpd.netty.tools.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import com.hpd.netty.model.JSONtype;
import com.hpd.netty.model.Type;
import com.hpd.netty.model.UserServerPojo;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 消息发送
 * 
 * @author tdz
 * @date 2016年12月12日
 *
 */
public final class Msg {
	private TextWebSocketFrame msg;// webSocket消息体
	private ChannelHandlerContext ctx;
	@SuppressWarnings("unused")
	private ChannelGroup group;

	public Msg(ChannelGroup group, TextWebSocketFrame msg, ChannelHandlerContext ctx) {
		this.msg = msg;
		this.ctx = ctx;
		this.group = group;
	}

	/**
	 * 全体广播
	 * 
	 * @author tdz
	 * @date 2016年12月12日
	 */
	public void sendAll() {

	}

	/**
	 * 目标广播
	 * 
	 * @author tdz
	 * @date 2016年12月12日
	 */
	public void sendTo() {
		JSONObject json = new JSONObject();
		try {
			JSONObject msgJson = JSONObject.parseObject(msg.text());
			String info = msgJson.getString("info");
			// info信息验证
			if (info == null || info.trim().equals("")) {
				json.put("result", SysCode.msg_isNull);
			}else if (info.length() > Integer.parseInt(Data.sysConfig.get("maxMsg").toString())) {
				json.put("result", SysCode.msg_LengthError);
			}else {
				msgCore(json, info, msgJson);
			}
//		} catch (JSONException e) {
//			json.put("result", ServerLog.print(Type.ERROR, e, SysCode.json_FormatError));
		} catch (Exception e) {
			json.put("result", ServerLog.print(Type.ERROR, e, SysCode.sys_unknownException));
		}
		/**
		 * 返回给发送者
		 */
		json.put("type", JSONtype.MSGTO);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));

	}

	/**
	 * 消息核心转发
	 * 
	 * @param json
	 * @param info
	 * @author tdz
	 * @date 2016年12月14日
	 */
	private void msgCore(JSONObject json, String info, JSONObject msgJson) {
        info = keyWords(info);
		// 当前登录用户对象
		UserServerPojo usPojo = Data.onlineUser.get(msgJson.getString("from"));//ctx.channel().id().toString()
		if (null == usPojo){
			Iterator<Map.Entry<String, UserServerPojo>> it = Data.onlineCustomer.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, UserServerPojo> entry = it.next();
				//if(entry.getValue().getUserChannel().id().toString().equals(ctx.channel().id().toString())){
				if(entry.getKey().equals(msgJson.getString("from"))){
                    usPojo = entry.getValue();
					break;
				}
			}
		}

		if (null == usPojo)
			// 未登录用户
			json.put("result", SysCode.user_NotLogin);
		else {
			// 客服判断to
			String from = null;
			String fromName = null;
            Msginfo msginfo = null;
			Channel toChannel = null;
			if (usPojo.getUserRole() == 1) {
                fromName = usPojo.getUserName();
                from = usPojo.getAcount();//getUserChannel().id().toString();
                if (null != Data.onlineUser.get(msgJson.getString("to"))){
                    toChannel = Data.onlineUser.get(msgJson.getString("to")).getUserChannel();
                    msginfo = DbService.saveMsg(usPojo.getCheckinInn(), Data.onlineUser.get(msgJson.getString("to")).getUserId(), fromName, usPojo.getUserId(), false, info, false);
                }else{
                    msginfo = DbService.saveMsg(usPojo.getCheckinInn(), msgJson.getString("to"), fromName, usPojo.getUserId(), false, info, false);
                }
				if (fromName == null) {
					json.put("result", SysCode.customer_userNull);
					return;
				}
			} else if (usPojo.getUserRole() == 2) {
				// 用户直接取
//				if (usPojo.getCustomerChannel() == null) {
//					// 还未分配客服无法通讯
//					json.put("result", SysCode.user_customerNull);
//					return;
//				} else {
//					fromName = usPojo.getUserName();
//					toChannel = usPojo.getCustomerChannel();
//				}
				//指定客服
				UserServerPojo toUserServerPojo = Data.onlineCustomer.get(msgJson.getString("to"));
				if(null != toUserServerPojo && null != toUserServerPojo.getUserChannel()){
					fromName = usPojo.getUserName();
                    from = usPojo.getAcount();//.getUserChannel().id().toString();
					toChannel = toUserServerPojo.getUserChannel();

                    msginfo = DbService.saveMsg(toUserServerPojo.getCheckinInn(), usPojo.getUserId(), fromName, toUserServerPojo.getUserId(), true, info, false);
				}else{
                    msginfo = DbService.saveMsg(msgJson.getString("checkinInn"), usPojo.getUserId(), fromName, null, true, info, false);

                    JSONObject msgFrom = new JSONObject();
                    msgFrom.put("info", msginfo.getMsgContent());
                    msgFrom.put("sendtime", StringUtils.date2Str(msginfo.getChatTime(), StringUtils.DATE_TIME_FORMAT));
                    json.put("originalMsg", msgFrom);
                    json.put("result", SysCode.success);
                    json.put("type", JSONtype.MSGTO);
                    ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));

                    // 还未分配客服无法通讯
                    json.put("result", SysCode.user_customerNull);
                    json.remove("originalMsg");
					return;
				}
			}

            if (null!=toChannel){
                JSONObject msgFrom = new JSONObject();
                msgFrom.put("type", JSONtype.MSGFROM);
                if (null!=msginfo){
                    msgFrom.put("id", msginfo.getId());
                }
                msgFrom.put("info", info);
                msgFrom.put("from", from);
				msgFrom.put("is_read", false);
                msgFrom.put("fromName", fromName);
                msgFrom.put("sendtime", StringUtils.date2Str(new Date(), StringUtils.DATE_TIME_FORMAT));//
                toChannel.writeAndFlush(new TextWebSocketFrame(msgFrom.toString()));
                json.put("result", SysCode.success);
                json.put("originalMsg", msgFrom);
            }else{
                json.put("result", SysCode.sys_unknownException);
            }
		}

	}

//    /**
//     * 找到服务人数最少的客服
//     */
//    private UserServerPojo findCustomer(UserServerPojo usPojo) {
//        UserServerPojo toUserServerPojo = null;
//        if(null == usPojo){
//            return  null;
//        }
//
//        String channelId = null;
//        int size = Integer.parseInt(Data.sysConfig.get("customerQueue").toString());
//        Iterator<Map.Entry<String, UserServerPojo>> it = Data.onlineCustomer.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, UserServerPojo> entry = it.next();
////            if(entry.getKey().equals(checkinInn)){
////                channelId = entry.getKey();
////                break;
////            }
//
//            if (entry.getValue().getCheckinInn().equals(checkinInn)){
//                if (entry.getValue().getCustomerThread().size() == 0) {
//                    channelId = entry.getKey();
//                    break;
//                } else if (entry.getValue().getCustomerThread().size() < size) {
//                    size = entry.getValue().getCustomerThread().size();
//                    channelId = entry.getKey();
//                }
//            }
//        }
//        return channelId;
//    }

	/**
	 * 敏感字符过滤
	 * 
	 * @param msg
	 * @author tdz
	 * @date 2016年12月12日
	 */
	private String keyWords(String msg) {
		return msg;
	}
}
