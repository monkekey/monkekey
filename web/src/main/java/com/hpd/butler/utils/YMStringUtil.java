package com.hpd.butler.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zy on 7/22/2017.
 */
public class YMStringUtil {

    public static String getExtensionName(String filename) {
        if (!StringUtils.isEmpty(filename)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot);
            }
        }
        return "";
    }

    /**
     * 获取对应长度的随机字符串
     * @param length
     * @return
     */
    public static String getNonceStr(int length){
        String souStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int maxLen = souStr.length();
        Random r = new Random();
        StringBuilder str = new StringBuilder();
        for(int i=0;i<length;i++){
            str.append(souStr.charAt(r.nextInt(maxLen)));
        }
        return str.toString();
    }

    /**
     * 对象转map
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }

        return map;
    }
}
