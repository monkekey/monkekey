package com.hpd.netty.tools;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_1 = "yyyy/MM/dd";
	public static final String DATE_FORMAT_2 = "dd/MM/yyyy";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String LONG_DATE_TIME_FORMAT = "yyMMddhhmmssSSS";
	
	public static String isNull(String str) {
		return null == str ? "" : str;
	}
	
	public static boolean isNULL(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		return false;
	}

	public static String sortByChart(String str){
		return Stream.of(str.split(""))
				.sorted()
				.collect(Collectors.joining());
	}
	
	public static String ISOToUTF8(String str) {
		String reStr ="";
		if(isNULL(str)) return reStr;
		try {
			 reStr = new String(str.getBytes("iso8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return reStr;
	}
	/**
	 * 获取对应长度的随机字符串
	 * @param length
	 * @return
	 */
	public static String getNonceStr(int length, boolean onlyDigit){
		String souStr = "abcdefghijklmnopqrstuvwxyz0123456789";
		if (onlyDigit){
			souStr = "0123456789";
		}
		int maxLen = souStr.length();
		Random r = new Random();
		StringBuilder str = new StringBuilder();
		for(int i=0;i<length;i++){
			str.append(souStr.charAt(r.nextInt(maxLen)));
		}
		return str.toString();
	}
	
	/**
	 * 连接字符串
	 * @param strList
	 * @return
	 */
	public static String concatStr(List<String> strList){
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < strList.size(); i++) {
			str.append(strList.get(i));
		}
		return str.toString();
	}
	/**
	 * 连接字符串
	 * @param strList
	 * @return
	 */
	public static String concatStr(String[] strArr){
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < strArr.length; i++) {
			str.append(strArr[i]);
		}
		return str.toString();
	}
	
	/**
	 * return Date of input string
	 * @param s String
	 * @return String
	 */			
	public static java.util.Date str2Date(String s){
		return str2Date(s, DATE_FORMAT);
	}
		
	/**
	 * return Date of input string
	 * @param s String
	 * @param format String
	 * @return String
	 */			
	public static java.util.Date str2Date(String s, String format){
		java.util.Date dRet = null;
		SimpleDateFormat sdf = null;
		try{
			sdf = new SimpleDateFormat(format);
			dRet = sdf.parse(s);
			return dRet ;
		}catch(ParseException pe){
		}
		try{
			sdf = new SimpleDateFormat(DATE_FORMAT);
			dRet = sdf.parse(s);
			return dRet ;
		}catch(ParseException pe){
		}
		try{
			sdf = new SimpleDateFormat(DATE_FORMAT_1);
			dRet = sdf.parse(s);
			return dRet ;
		}catch(ParseException pe){
		}
		try{
			sdf = new SimpleDateFormat(DATE_FORMAT_2);
			dRet = sdf.parse(s);
			return dRet ;
		}catch(ParseException pe){
		}
		
		return dRet;
	}

	/**
	 * return String of input date
	 * @param d Date
	 * @return String
	 */			
	public static String date2Str(java.util.Date d){
		return date2Str(d, DATE_FORMAT);
	}
	
	/**
	 * return String of input date
	 * @param d Date
	 * @param format String
	 * @return String
	 */			
	public static String date2Str(java.util.Date d, String format){
		if(d == null)
			return "";
			
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	/**
	 * 求得从某天开始，过了几年几月几日几时几分几秒后，日期是多少
	 * 几年几月几日几时几分几秒可以为负数
	 * @param date Date
	 * @param year int
	 * @param month int
	 * @param day int
	 * @param hour int
	 * @param min int
	 * @param sec int
	 * @return Date
	 */
	public static java.util.Date modifyDate(java.util.Date date,int year ,int month,int day,int hour,int min,int sec){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR,year);
		cal.add(Calendar.MONTH,month);
		cal.add(Calendar.DATE,day);
		cal.add(Calendar.HOUR,hour);
		cal.add(Calendar.MINUTE,min);
		cal.add(Calendar.SECOND,sec);

		return cal.getTime();

	}

	public static String getExtensionName(String filename) {
		if (!StringUtils.isNULL(filename)) {
			int dot = filename.lastIndexOf('.');
			if ((dot >-1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot);
			}
		}
		return "";
	}


	public static void main(String[] args){
		System.out.println(date2Str(modifyDate(new Date(), 0, 0, 0, 0, 0, 7200), DATE_TIME_FORMAT));
	}
}
