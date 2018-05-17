package com.hpd.butler.web;


import com.hpd.butler.common.RequestResult;
import com.hpd.butler.utils.UploaderAliyunOSS;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "uploadFileController",description = "上传文件相关")
@RequestMapping("/uploadFile")
public class uploadFileController {

    @Value("${ossCommodityImg.key}")
    private String oss_key ;
    @Value("${ossCommodityImg.secret}")
    private String oss_secret;
    @Value("${ossCommodityImg.endpoint}")
    private String oss_endpoint;
    @Value("${ossCommodityImg.bucket}")
    private String oss_bucket;

    @ApiOperation(value = "uploadController|上传文件")
    @PostMapping("/upload")
    public RequestResult uploadExcel(@RequestParam(value = "fileName",required = true)String fileName,
                                     @RequestParam(value = "upperFileName",required = true)String upperFileName,
                                     @RequestParam(value = "footFileName",required = true)String footFileName,
                                     @RequestParam(value = "file",required = true)MultipartFile uploadedFile) throws Exception {

        String uploadPath = "",fileType= "";
        uploadPath = String.format("%s/%s", upperFileName,footFileName);
        String filename = uploadedFile.getOriginalFilename();
        if (!StringUtils.isEmpty(filename)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                fileType = filename.substring(dot);
            }
        }
        String result = UploaderAliyunOSS.upload3Oss(uploadedFile.getInputStream(), fileType, uploadPath, oss_endpoint, oss_key, oss_secret, fileName);

        return RequestResult.success(result);
    }

}
