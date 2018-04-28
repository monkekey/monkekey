package com.yumi.netty.service.core;

import com.yumi.netty.model.Data;
import com.yumi.netty.model.JSONtype;
import com.yumi.netty.model.SysCode;
import com.yumi.netty.model.Type;
import com.yumi.netty.tools.ServerLog;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import com.yumi.netty.model.UserServerPojo;

import java.util.Iterator;
import java.util.Map;

/**
 * 容器清空
 * 
 * @author tdz
 * @date 2016年12月13日
 *
 */
public final class ContainerChange {

	/**
	 * 用户离线容器remove
	 * 
	 * @author tdz
	 * @date 2016年12月13日
	 */
	public final static void unReg(ChannelGroup group, ChannelHandlerContext ctx) {
		try {
			/**
			 * 系统事件不做非空
			 */
			group.remove(ctx.channel());
			clear(ctx.channel());
		} catch (NullPointerException e) {
			ServerLog.print(Type.ERROR, e, SysCode.remove_Error);
		} catch (Exception e) {
			ServerLog.print(Type.ERROR, e, SysCode.sys_unknownException);
		}
	}

	/**
	 * 本地重复登录检测,remove原目标
	 * 
	 * @author tdz
	 * @throws
	 * @date 2016年12月12日
	 */
	public void localLoginCheck(ChannelGroup group, ChannelHandlerContext ctx) {
		if (group.contains(ctx.channel()))
			group.remove(ctx.channel());
		clear(ctx.channel());
	}

	/**
	 * 异地重复登录检测(只涉及登录用户,访客不涉及)
	 * 异地登录channel不同,只能通过userId校验
	 * @author tdz
	 * @throws
	 * @date 2016年12月12日
	 */
	public void elesWhereLoginCheck(ChannelGroup group, String userId) {
		Channel channel = null;
		if (Data.idMapping.containsKey(userId)) {
			channel = Data.idMapping.get(userId);
			clear(channel);
			group.remove(channel.id());
			channel.closeFuture();
			channel.close();
		}
	}

	/**
	 * 容器统一清空
	 * 
	 * @param channel
	 * @author tdz
	 * @throws Exception
	 * @date 2016年12月13日
	 */
	private final static void clear(Channel channel) {
		String account = null, checkinInn = "";
		// 用户离线
		Iterator<Map.Entry<String, UserServerPojo>> userIt = Data.onlineUser.entrySet().iterator();
		while (userIt.hasNext()) {
			Map.Entry<String, UserServerPojo> userEntry = userIt.next();
			if(userEntry.getValue().getUserChannel().id() == channel.id() ){
				UserServerPojo usPojo = userEntry.getValue();
                account = usPojo.getAcount();
                checkinInn = usPojo.getCheckinInn();
				Data.onlineUser.remove(usPojo.getAcount());
				// 清空当前用户所在的客服状态
				if (usPojo.getCustomerChannel() != null) {

					Iterator<Map.Entry<String, UserServerPojo>> cusIt = Data.onlineCustomer.entrySet().iterator();
					while (cusIt.hasNext()) {
						Map.Entry<String, UserServerPojo> cusEntry = cusIt.next();
                        if (cusEntry.getValue().getCheckinInn().equals(checkinInn)) {
                            for (Channel channel1 : cusEntry.getValue().getCustomerThread()) {
                                if (channel1.id() == channel.id()) {
                                    cusEntry.getValue().getCustomerThread().remove(channel1);
                                    break;
                                }
                            }
                            break;
                        }
					}

					//Data.onlineCustomer.get(usPojo.getCustomerChannel().id().toString()).getCustomerThread().remove(channel);
					// 通知客服用户离开
					JSONObject json = new JSONObject();
					json.put("type", JSONtype.USERLEAVE);
					json.put("userId", account);
					usPojo.getCustomerChannel().writeAndFlush(new TextWebSocketFrame(json.toString()));
				}
				break;
			}
		}


		Iterator<Map.Entry<String, UserServerPojo>> cusIt = Data.onlineCustomer.entrySet().iterator();
		while (cusIt.hasNext()) {
			Map.Entry<String, UserServerPojo> cusEntry = cusIt.next();
			if(cusEntry.getValue().getUserChannel().id() == channel.id() ){
				// 客服离线
				UserServerPojo cusPojo = cusEntry.getValue();
				account = cusPojo.getAcount();
                // 清空当前客服服务的用户
                for (Channel userChannel : cusPojo.getCustomerThread()){
                    userIt = Data.onlineUser.entrySet().iterator();
                    while (userIt.hasNext()) {
                        Map.Entry<String, UserServerPojo> userEntry = userIt.next();
                        if (userEntry.getValue().getCustomerChannel().id() == userChannel.id()){
                            Data.onlineUser.remove(userEntry.getValue().getUserChannel());
                            break;
                        }
                    }
                }
				Data.onlineCustomer.remove(cusPojo.getAcount());
                break;
			}
		}


//		if (Data.onlineUser.containsKey(channel.id().toString())) {
//			UserServerPojo usPojo = Data.onlineUser.get(channel.id().toString());
//			uId = usPojo.getUserId();
//			Data.onlineUser.remove(channel.id().toString());
//			// 清空当前用户所在的客服状态
//			if (usPojo.getCustomerChannel() != null && Data.onlineCustomer.containsKey(usPojo.getCustomerChannel().id().toString())) {
//				Data.onlineCustomer.get(usPojo.getCustomerChannel().id().toString()).getCustomerThread().remove(channel);
//				// 通知客服用户离开
//				JSONObject json = new JSONObject();
//				json.put("type", JSONtype.USERLEAVE);
//				json.put("userId", uId);
//				usPojo.getCustomerChannel().writeAndFlush(new TextWebSocketFrame(json.toString()));
//			}
//		} else if (Data.onlineCustomer.containsKey(channel.id().toString())) {
//			// 客服离线
//			UserServerPojo usPojo = Data.onlineCustomer.get(channel.id().toString());
//			uId = usPojo.getUserId();
//			Data.onlineCustomer.remove(channel.id().toString());
//			// 清空当前客服服务的用户
//			int size = usPojo.getCustomerThread().size();
//			for (int i = 0; i < size; i++) {
//				if (Data.onlineUser.containsKey(usPojo.getCustomerThread().get(i).id().toString())) {
//					UserServerPojo temp = Data.onlineUser.get(usPojo.getCustomerThread().get(i).id().toString());
//					temp.setCustomerChannel(null);
//					// 通知用户客服离开
//					JSONObject json = new JSONObject();
//					json.put("type", JSONtype.CUSTOMERLEAVE);
//					temp.userChannel.writeAndFlush(new TextWebSocketFrame(json.toString()));
//				}
//			}
//		}
		if (Data.serverQueue.indexOf(channel) != -1)
		{
			Data.serverQueue.remove(channel);
			ServerThread.sendServerThreadPosition();
			
		}
		
		Data.idMapping.remove(account);
	}

	
}
