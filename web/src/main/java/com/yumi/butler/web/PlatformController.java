package com.yumi.butler.web;

import com.yumi.butler.common.RequestResult;
import com.yumi.butler.service.FileService;
import com.yumi.butler.service.PlatformService;
import com.yumi.butler.utils.UploaderAliyunOSS;
import com.yumi.netty.tools.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

@RestController
@RequestMapping("/platform")
@Api(value = "PlatformController", description = "自助入住相关接口")
public class PlatformController {

    @Autowired
    private PlatformService platformService;


    @ApiOperation(value = "IDCard上传 | File")
    @PostMapping("/uploadIDCard")
    public RequestResult uploadIDCard(@RequestParam(value = "uploadingFile", required = true) MultipartFile uploadedFile,
                                    @RequestParam(value = "orderNo", required = true) String orderNo) throws Exception {

        RequestResult result = platformService.getIDCard(uploadedFile,orderNo);

        return result;
    }


    @ApiOperation(value = "face上传 | File")
    @PostMapping("/uploadFace")
    public RequestResult uploadFace(@RequestParam(value = "uploadingFile", required = true) MultipartFile uploadedFile,
                                    @RequestParam(value = "orderNo", required = true) String orderNo,
                                    @RequestParam(value = "IDurl", required = true) String IDurl) throws Exception {

        RequestResult result = platformService.getFace(uploadedFile,IDurl,orderNo);

        return result;
    }




}
