package com.hpd.netty.tools;

import com.alibaba.fastjson.JSON;
import com.hpd.netty.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 日志打印
 * @author tdz
 * @date 2016年12月9日
 *
 */
public final class ServerLog {
	private static Logger logger = LoggerFactory.getLogger(ServerLog.class);

	/**
	 * 输出str
	 * @param t
	 * @param ob
	 * @author tdz
	 * @date 2016年12月9日
	 */
	public final static void print(Type t, Object... ob) {
		StringBuffer sbf = new StringBuffer(t.getVal());
		for (Object temp : ob)
			sbf.append(JSON.toJSONString(temp));
		logger.info(sbf.toString());
	}
	/**
	 * 输出str 且返回
	 * @param t
	 * @param val
	 * @author tdz
	 * @date 2016年12月9日
	 */
	public  final static int print(Type t, int val) {
		StringBuffer sbf = new StringBuffer(t.getVal());
		sbf.append(String.valueOf(val));
		logger.info(sbf.toString());
		return val;
	}
	/**
	 * 输出堆栈
	 * @param t
	 * @param ob
	 * @author tdz
	 * @date 2016年12月9日
	 */
	public  final static void print(Type t, Throwable e, Object... ob) {
		StringBuffer sbf = new StringBuffer(t.getVal());
		for (Object temp : ob)
			sbf.append(temp);
		logger.info(sbf.toString(), e);
	}
	/**
	 * 输出堆栈 且返回
	 * @param t
	 * @param val
	 * @author tdz
	 * @date 2016年12月9日
	 */
	public  final static int print(Type t, Throwable e, int val) {
		StringBuffer sbf = new StringBuffer(t.getVal());
		sbf.append(String.valueOf(val));
		logger.info(sbf.toString(), e);
		return val;
	}
}
