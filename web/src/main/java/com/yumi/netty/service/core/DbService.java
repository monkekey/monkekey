package com.yumi.netty.service.core;

import com.yumi.butler.domain.Msginfo;
import com.yumi.butler.service.MsgInfoService;
import com.yumi.butler.utils.SpringUtil;
import com.yumi.butler.vo.UserVo;
import com.yumi.netty.tools.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.yumi.butler.service.SysUserService;

import java.util.Date;


/**
 * DB操作
 *  
 * @author tdz
 * @date 2016年12月9日
 *
 */
public class DbService {
    private static SysUserService sysUserService = SpringUtil.getBean(SysUserService.class);
	private static MsgInfoService msgInfoService = SpringUtil.getBean(MsgInfoService.class);

	/**
	 * 保存消息
	 * 
	 * @return
	 * @author tdz
	 * @date 2016年12月9日
	 */
	public static Msginfo saveMsg(String checkininn, String openid, String guestName, String butler, boolean fromGuest, String info, boolean isRead) {
		if (null == msgInfoService){
			msgInfoService = SpringUtil.getBean(MsgInfoService.class);
		}

        Msginfo msginfo = new Msginfo();
        msginfo.setCheckinInn(checkininn);
        msginfo.setOpenid(openid);
        msginfo.setGuestName(guestName);
        msginfo.setButlerAccount(butler);
        msginfo.setFromGuest(fromGuest ? "1" : "0");
        msginfo.setMsgContent(info);
		msginfo.setIsRead(isRead ? 1 : 0);
        msginfo.setChatTime(new Date());

        return msgInfoService.saveMsgInfo(msginfo);
	}

	/**
	 * 验证用户信息
	 * @param msg
	 * @return
	 * @author tdz
	 * @date 2016年12月9日
	 */
	@SuppressWarnings("unused")
	public static JSONObject checkUser(String msg) throws NullPointerException, IndexOutOfBoundsException {
		// 用户账号#密码MD5-64bit
		JSONObject userJson = JSONObject.parseObject(msg);
		String account = userJson.getString("account");

        if(StringUtils.isNULL(account)){
            String passwordMd5 = userJson.getString("password");
            String userHead = userJson.getString("userHead");
            String checkinRoom = userJson.getString("checkinRoom");
            String nickName = userJson.getString("nickName");

            userJson.put("account", userJson.getString("userId"));
            userJson.put("userName", StringUtils.isNULL(checkinRoom)? nickName : (checkinRoom + "房" + nickName));
            userJson.put("userHead", userHead==null?"http://oh39r65yn.bkt.clouddn.com/5030aff5074dd.jpg":userHead);
            //userJson.put("userId", new Random().nextInt(89999)+10000);//传过来的，取了openid
            userJson.put("userRole", "2");

        }else{
            // 调用check验证用户身份 账号密码匹配数据库
            userJson.clear();
            if(null == sysUserService){
                sysUserService = SpringUtil.getBean(SysUserService.class);
            }
            UserVo sysuser = sysUserService.findByAccount(account);

            if (null != sysuser && null != sysuser.getInnCode()) {
                userJson.put("account", sysuser.getAccount());
                userJson.put("userName", sysuser.getUserName());
                userJson.put("userHead", sysuser.getFaces());
                userJson.put("userId", sysuser.getId());
                userJson.put("userRole", "1"); // 1客服 2用户

            }
        }
		return userJson;
	}

	/**
	 * 离开聊天室
	 * 
	 * @return
	 * @author tdz
	 * @date 2016年12月9日
	 */
	public static String leave() {
		return null;
	}
}
