package com.hpd.butler.api;


import com.alibaba.fastjson.JSONObject;
import com.hpd.butler.common.RequestResult;
import com.hpd.butler.utils.LocalHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

/**
 * Created by Leo on 2017/8/29.
 */
public class BaseAPI {
    protected static final String BASE_URI = "https://api.weixin.qq.com";
    protected static final String MCH_URI = "https://api.mch.weixin.qq.com";

    protected static Header jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
    protected static Header xmlHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,ContentType.APPLICATION_XML.toString());


    protected static final LocalHttpClient localHttpClient = LocalHttpClient.getInstance();

    protected RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(1000*60)//设置传输超时时间
            .setConnectTimeout(1000*30)//设置请求超时时间
            .build();

    protected RequestResult checkData(String res, String apiName){
        System.out.println(res);
        if(StringUtils.isEmpty(res)){

            return new RequestResult().fail("接口数据错误,请重试");
        }else{
            try{
                JSONObject data = JSONObject.parseObject(res);
                return new RequestResult().success(data);
            }catch(Exception e){
                return new RequestResult().fail("获取到的数据格式有误,请重试");
            }
        }
    }
}
