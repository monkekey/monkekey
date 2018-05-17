package com.hpd.butler.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploaderAliyunOSS {
    /**
     * 上传到阿里云OSS
     * @param file
     * @param uploadPah
     * @param point
     * @param key
     * @param secret
     * @param bucket
     * @return  List<String> 图片全链接
     */
    public static List<String> upload2Oss(File file, String uploadPah, String point, String key, String secret, String bucket){
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(point, key, secret);
        // 获取Bucket的存在信息
        boolean bucketExist = client.doesBucketExist(bucket);
        if (!bucketExist)//不存在这创建一个bucket
            client.createBucket(bucket);
        List<String> fileUrlList = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");


        try {
            String fileName = uploadPah+"/"+format.format(new Date())+".jpg";
            //获取文件输入流
            FileInputStream content = new FileInputStream(file);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(file.length());

            // 上传Object
            client.putObject(bucket, fileName, content, meta);

            content.close();

            fileUrlList.add("https://"+bucket+".oss-cn-hangzhou.aliyuncs.com/"+fileName);

            return fileUrlList;
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传到阿里云OSS
     * @param file
     * @param ext
     * @param uploadPah
     * @param point
     * @param key
     * @param secret
     * @param bucket
     * @return  List<String> 路径链接
     */
    public static String upload3Oss(InputStream file, String ext, String uploadPah, String point, String key, String secret, String bucket){
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(point, key, secret);
        // 获取Bucket的存在信息
        boolean bucketExist = client.doesBucketExist(bucket);
        if (!bucketExist)//不存在这创建一个bucket
            client.createBucket(bucket);
        String fileUrl = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            String filePath = uploadPah+"/"+format.format(new Date())+ RandomStringUtils.random(4, false, true) + ext;
            //获取文件输入流
            InputStream content = file;
            // 创建上传Object的Meta
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(content.available());
            // 上传Object
            client.putObject(bucket, filePath, content, meta);

            content.close();

            //fileUrl = "https://"+bucket+".oss-cn-shenzhen.aliyuncs.com/"+filePath;
            //return String.format("https://%s.%s/%s", bucket, point, filePath);
            return filePath;
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传到阿里云OSS
     * @param file
     * @param ext
     * @param uploadPah
     * @param point
     * @param key
     * @param secret
     * @param bucket
     * @return  List<String> 路径链接
     */
    public static String upload3OssProject(InputStream file, String ext, String uploadPah, String point, String key, String secret, String bucket,String department){
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(point, key, secret);
        // 获取Bucket的存在信息
        boolean bucketExist = client.doesBucketExist(bucket);
        if (!bucketExist)//不存在这创建一个bucket
            client.createBucket(bucket);
        String fileUrl = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            String filePath = uploadPah+"/" + department +format.format(new Date())+ RandomStringUtils.random(4, false, true) + ext;
            //获取文件输入流
            InputStream content = file;
            // 创建上传Object的Meta
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(content.available());
            // 上传Object
            client.putObject(bucket, filePath, content, meta);

            content.close();

            //fileUrl = "https://"+bucket+".oss-cn-shenzhen.aliyuncs.com/"+filePath;
            //return String.format("https://%s.%s/%s", bucket, point, filePath);
            return filePath;
        } catch (OSSException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteImg(String filePath,String point,String key,String secret,String bucket) {
        OSSClient ossClient = new OSSClient(point, key, secret);
        // 删除Object
        ossClient.deleteObject(bucket, filePath);
        // 关闭client
        ossClient.shutdown();
    }
}
