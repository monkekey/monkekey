package com.yumi.butler.utils;

import java.math.BigDecimal;

/**
 * Created by zy on 2018/1/3.
 */
public class CoodinateCovertor {
    // 地球半径
    private static final double EARTH_RADIUS = 6370996.81;

    private static double x_pi = Math.PI * 3000.0 / 180.0;

    /**
     * 对double类型数据保留小数点后多少位
     *  高德地图转码返回的就是 小数点后6位，为了统一封装一下
     * @param digit 位数
     * @param in 输入
     * @return 保留小数位后的数
     */
    static double dataDigit(int digit,double in){
        return new BigDecimal(in).setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 将火星坐标转变成百度坐标
     * @param lngLat_gd 火星坐标（高德、腾讯地图坐标等）
     * @return 百度坐标
     */

    public static LngLat bd_encrypt(LngLat lngLat_gd)
    {
        double x = lngLat_gd.getLongitude(), y = lngLat_gd.getLantitude();
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x *  x_pi);
        return new LngLat(dataDigit(6,z * Math.cos(theta) + 0.0065),dataDigit(6,z * Math.sin(theta) + 0.006));

    }
    /**
     * 将百度坐标转变成火星坐标
     * @param lngLat_bd 百度坐标（百度地图坐标）
     * @return 火星坐标(高德、腾讯地图等)
     */
    static LngLat bd_decrypt(LngLat lngLat_bd)
    {
        double x = lngLat_bd.getLongitude() - 0.0065, y = lngLat_bd.getLantitude() - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        return new LngLat( dataDigit(6,z * Math.cos(theta)),dataDigit(6,z * Math.sin(theta)));
    }

    // 弧度
    private static double radian(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @Description: 获取俩点距离
     * @param lnglat1
     * @param lnglat2
     * @return void
     * @date 2014-9-7 上午10:11:55
     */
    public static int calculateDistance(LngLat lnglat1, LngLat lnglat2) {
        double radLat1 = radian(lnglat1.getLantitude());
        double radLat2 = radian(lnglat2.getLantitude());
        double a = radLat1 - radLat2;
        double b = radian(lnglat1.getLongitude()) - radian(lnglat2.getLongitude());
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        double ss = s * 1.0936132983377;
        //System.out.println("两点间的距离是：" + s + "米" + "," + (int) ss + "码");
        return (int)s;
    }

    //测试代码
    public static void main(String[] args) {
//        LngLat lngLat_bd = new LngLat(113.3315438032, 23.1312339543);
//        System.out.println(bd_encrypt(lngLat_bd));
        System.out.println(calculateDistance(new LngLat(113.3386498536, 23.1389022892),new LngLat(113.3307999105, 23.1401860671)));
    }
}
