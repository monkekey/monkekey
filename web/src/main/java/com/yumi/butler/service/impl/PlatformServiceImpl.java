package com.yumi.butler.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yumi.butler.common.RequestResult;
import com.yumi.butler.service.PlatformService;
import com.yumi.butler.utils.AliyunUtil;
import com.yumi.butler.utils.UploaderAliyunOSS;
import com.yumi.netty.tools.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.awt.*;
import java.io.InputStream;
import java.util.Date;

@Service
public class PlatformServiceImpl implements PlatformService {

    @Value("${oss.key}")
    private String oss_key ;
    @Value("${oss.secret}")
    private String oss_secret;
    @Value("${oss.endpoint}")
    private String oss_endpoint;
    @Value("${oss.bucket}")
    private String oss_bucket;
    @Value("${aliyun.appcode}")
    private String aliyun_appcode;

    public RequestResult getIDCard(MultipartFile uploadedFile, String orderNo){

        //上传aliyunOSS
        String filePath = "order-guest/"  + orderNo;
        String fileExt  = StringUtils.getExtensionName(uploadedFile.getOriginalFilename());
        String file = "";
        try {
            file = UploaderAliyunOSS.upload3Oss(uploadedFile.getInputStream(), fileExt, filePath, oss_endpoint, oss_key, oss_secret, oss_bucket);
        }catch (Exception e){
            e.printStackTrace();
            return RequestResult.fail("");
        }
        String fileUrl = "https://"+oss_bucket+"."+oss_endpoint+"/"+file;


        try {
            //身份证识别-----------
            byte[] data = null;
            // 读取图片字节数组
            InputStream in = uploadedFile.getInputStream();
            data = new byte[in.available()];
            in.read(data);
            in.close();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            String base64Img = encoder.encode(data);// 返回Base64编码过的字节数组字符串

            String appcode = "";
            JSONObject jsonb = new JSONObject();
            JSONObject configure = new JSONObject();
            String side = "face";//"back"反面、"face"正面
            configure.put("side", side);
            jsonb.put("image", base64Img);
            jsonb.put("configure", configure);
            String bodys = jsonb.toString();
            System.out.println("request body:" + bodys);

            //获取身份证信息
            String result = AliyunUtil.getIDCard(aliyun_appcode, bodys);
            JSONObject jsonres = JSONObject.parseObject(result);
            jsonres.put("IDurl", fileUrl);
            //存数据库
            return RequestResult.success(jsonres);
        }catch (Exception e){
            e.printStackTrace();
            return RequestResult.fail(fileUrl);
        }

    }


    public RequestResult getFace(MultipartFile uploadedFile, String IDurl, String orderNo){

        //上传
        String filePath = "order-guest/"  + orderNo;
        String fileExt  = StringUtils.getExtensionName(uploadedFile.getOriginalFilename());
        String file = "";
        try{
            file = UploaderAliyunOSS.upload3Oss(uploadedFile.getInputStream(), fileExt, filePath, oss_endpoint, oss_key, oss_secret, oss_bucket);
        }catch (Exception e){
            e.printStackTrace();
            return RequestResult.fail("");
        }
        String fileUrl = "https://"+oss_bucket+"."+oss_endpoint+"/"+file;

        try {
            Thread.sleep(3000);
            //人脸识别
            String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/face/verify";
            JSONObject json = new JSONObject();
            json.put("type", 0);
            json.put("image_url_1", fileUrl);
            json.put("image_url_2", IDurl);
            String result = "";
            try {
                result = AliyunUtil.sendPost(url, json.toJSONString(), oss_key, oss_secret);
            } catch (Exception e) {
                e.printStackTrace();
                return RequestResult.fail(fileUrl);
            }
            if (StringUtils.isNULL(result)) {
                return RequestResult.fail(fileUrl);
            }
            JSONObject jsonres = JSONObject.parseObject(result);
            jsonres.put("faceUrl", fileUrl);
            return RequestResult.success(jsonres);
        }catch (Exception e){
            e.printStackTrace();
            return RequestResult.fail(fileUrl);
        }
    }
}
