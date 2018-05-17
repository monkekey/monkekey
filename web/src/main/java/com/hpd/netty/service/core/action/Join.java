package com.hpd.netty.service.core.action;

import com.hpd.butler.domain.Msginfo;
import com.hpd.butler.service.MsgInfoService;
import com.hpd.butler.utils.SpringUtil;
import com.hpd.butler.vo.Guest;
import com.hpd.netty.model.*;
import com.hpd.netty.service.core.ContainerChange;
import com.hpd.netty.service.core.DbService;
import com.hpd.netty.service.core.ServerThread;
import com.hpd.netty.tools.ServerLog;
import com.hpd.netty.tools.StringUtils;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 进入聊天室
 * 
 * @author tdz
 * @date 2016年12月9日
 *
 */
public final class Join {
	private TextWebSocketFrame msg;// webSocket消息体
	private ChannelHandlerContext ctx;
	private ChannelGroup group;

	public Join(ChannelGroup group, TextWebSocketFrame msg, ChannelHandlerContext ctx) {
		this.msg = msg;
		this.ctx = ctx;
		this.group = group;
	}

	/**
	 * 加入聊天室
	 * 
	 * @param json
	 * @param type
	 * @author tdz
	 * @date 2016年12月12日
	 */
	public void execute(JSONObject json, String type) {
		// 新用户链接消息加入
		try {
			new ContainerChange().localLoginCheck(group, ctx);
			UserServerPojo usPojo = new UserServerPojo();
			int flag = SysCode.user_NotFound;

			if (type.equals(JSONtype.JOIN))
				flag = user(usPojo, ctx.channel(), msg.text());
			// 数据正确
			if (flag == SysCode.success) {
				JSONObject userJson = bindUserInfo(usPojo);
				json.put("type", type);
				json.put("result", flag);
				json.put("info", userJson);
				
				// 用户登录
				if (usPojo.getUserRole() == 2) {
					//Data.onlineUser.put(usPojo.getUserChannel().id().toString(), usPojo);
					Data.onlineUser.put(userJson.getString("account"), usPojo);	//user
					new ServerThread().execute(json, ctx.channel());

					//发送离线消息
					MsgInfoService msgInfoService = SpringUtil.getBean(MsgInfoService.class);
					List<Msginfo> msgList = msgInfoService.findUnReadyMsg(userJson.getString("account"));
					for (Msginfo msg : msgList){
						JSONObject msgFrom = new JSONObject();
						msgFrom.put("type", JSONtype.MSGFROM);
						msgFrom.put("id", msg.getId());
						msgFrom.put("info", msg.getMsgContent());
						msgFrom.put("from", msg.getButlerAccount());
						msgFrom.put("fromName", msg.getGuestName());
						msgFrom.put("is_read", msg.getIsRead()==1);
						msgFrom.put("sendtime", StringUtils.date2Str(msg.getChatTime(), StringUtils.DATE_TIME_FORMAT));

						if (msg.getFromGuest().equals("1")) {
							json.put("result", SysCode.success);
							json.put("type", JSONtype.MSGTO );
							json.put("originalMsg", msgFrom);
							ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
						}else{
							ctx.channel().writeAndFlush(new TextWebSocketFrame(msgFrom.toString()));
						}
					}

				} else if (usPojo.getUserRole() == 1) {
					//客服登录
					if(!StringUtils.isNULL(userJson.getString("checkinInn"))){
						Data.onlineCustomer.put(userJson.getString("account"), usPojo);//userJson.getString("checkinInn")
					}
					ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
					//重新绑定客人与管家关系
                    Iterator<Map.Entry<String, UserServerPojo>> userIt = Data.onlineUser.entrySet().iterator();
                    while (userIt.hasNext()) {
                        Map.Entry<String, UserServerPojo> userEntry = userIt.next();
                        if (null!=userEntry.getValue().getCustomer() && userEntry.getValue().getCustomer().equals(userJson.getString("account"))){
                            JSONObject customerJson = new JSONObject();
                            customerJson.put("type", JSONtype.USERCOMING);
                            customerJson.put("user", bindUserInfo(userEntry.getValue()));
                            ctx.channel().writeAndFlush(new TextWebSocketFrame(customerJson.toString()));
                            userEntry.getValue().setCustomerChannel(ctx.channel());
                            break;
                        }
                    }

					//发送离线消息
					MsgInfoService msgInfoService = SpringUtil.getBean(MsgInfoService.class);
					List<Guest> guests = msgInfoService.findUnReadMsgGuest(userJson.getString("checkinInn"));
					for (Guest guest : guests){
						JSONObject customerJson = new JSONObject();

						JSONObject tuserJson = new JSONObject();
						tuserJson.put("userId", guest.getOpenid());
						tuserJson.put("userName", guest.getUserName());
						tuserJson.put("account", guest.getOpenid());

						customerJson.put("type", JSONtype.USERCOMING);
						customerJson.put("user", tuserJson);
						ctx.channel().writeAndFlush(new TextWebSocketFrame(customerJson.toString()));
					}

					List<Msginfo> msgList = msgInfoService.findUnReadyGuestMsg(userJson.getString("checkinInn"));
					for (Msginfo msg : msgList){
						JSONObject msgFrom = new JSONObject();
						msgFrom.put("type", JSONtype.MSGFROM);
						msgFrom.put("id", msg.getId());
						msgFrom.put("info", msg.getMsgContent());
						msgFrom.put("from", msg.getFromGuest().equals("1")?msg.getOpenid():msg.getButlerAccount());
						msgFrom.put("fromName", msg.getGuestName());
						msgFrom.put("to", !msg.getFromGuest().equals("1")?msg.getOpenid():msg.getButlerAccount());
						msgFrom.put("sendtime", StringUtils.date2Str(msg.getChatTime(), StringUtils.DATE_TIME_FORMAT));
						msgFrom.put("is_read", msg.getIsRead()==1);

						if (!msg.getFromGuest().equals("1")) {
							json.put("result", SysCode.success);
							json.put("type", JSONtype.MSGTO );
							json.put("originalMsg", msgFrom);
							ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
						}else{
							ctx.channel().writeAndFlush(new TextWebSocketFrame(msgFrom.toString()));
						}
					}

				}
				Data.idMapping.put(userJson.getString("account"), ctx.channel());
				// 新用户添加到channelGroup
				group.add(ctx.channel());
			} else {
				json.put("type", type);
				json.put("result", flag);
				ctx.channel().writeAndFlush(new TextWebSocketFrame(json.toString()));
			}
		} catch (NullPointerException e) {
			ServerLog.print(Type.ERROR, e, SysCode.remove_Error);
		} catch (Exception e) {
			ServerLog.print(Type.ERROR, e, SysCode.sys_unknownException);
		}
	}

	/**
	 * 聊天室（用户）
	 * 
	 * @param channel
	 * @param msg
	 * @return
	 * @author tdz
	 * @date 2016年12月9日
	 */
	private int user(UserServerPojo usPojo, Channel channel, String msg) {
		try {
			JSONObject json = JSONObject.parseObject(msg);
			// 注册用户
			JSONObject userJson = null;
			if ((userJson = DbService.checkUser(json.getString(JSONtype.INFO))) != null) {
				new ContainerChange().elesWhereLoginCheck(group, userJson.getString("userId"));
				usPojo.setAll(channel, userJson.getString("account"), userJson.getString("userName"), userJson.getString("userId"), userJson.getString("userHead"), json.getJSONObject("info").getString("checkinInn"), json.getJSONObject("info").getString("checkinRoom"), Integer.parseInt(userJson.getString("userRole")));
				if (Integer.parseInt(userJson.getString("userRole")) == 1) {
					// 客服登录初始化客服容器
					usPojo.ini();
				}
			} else {
				return ServerLog.print(Type.ERROR, SysCode.user_NotFound);
			}
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			return ServerLog.print(Type.ERROR, e, SysCode.json_FormatError);
		} catch (Exception e) {
			return ServerLog.print(Type.ERROR, e, SysCode.sys_unknownException);
		}
		return SysCode.success;
	}

	/**
	 * 组装用户信息到json
	 * 
	 * @param usPojo
	 * @return
	 * @author tdz
	 * @date 2016年12月13日
	 */
	private JSONObject bindUserInfo(UserServerPojo usPojo) {
		JSONObject userJson = new JSONObject();
		userJson.put("account", usPojo.getAcount());
		userJson.put("userName", usPojo.getUserName());
		userJson.put("userHead", usPojo.getUserHead());
		userJson.put("userId", usPojo.getUserId());
		userJson.put("userRole", usPojo.getUserRole());
        userJson.put("checkinInn", usPojo.getCheckinInn());
        userJson.put("checkinRoom", usPojo.getCheckinRoom());
		userJson.put("cId", ctx.channel().id().toString());
		return userJson;
	}
}
