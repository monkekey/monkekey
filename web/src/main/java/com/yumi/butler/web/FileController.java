package com.yumi.butler.web;

import com.yumi.butler.common.RequestResult;
import com.yumi.netty.tools.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.yumi.butler.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/10/11.
 */
@RestController
@RequestMapping("/file")
@Api(value = "FileController", description = "文件处理相关接口")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${storepath.wxacode}")
    private String wxacodeUrl;

    @ApiOperation(value = "单个文件上传 | File")
    @PostMapping("/uploadFile")
    public RequestResult uploadFile(@RequestParam(value = "uploadingFile", required = true) MultipartFile uploadedFile,
                                    @RequestParam(value = "fileType", required = false, defaultValue = "other") String fileType) throws Exception {
        //msg:消息   图、音频
        //userHead: 用户头像
        //notice:   公告
        //other:
        if (StringUtils.isNULL(fileType) || "msg, userHead, notice, service, goodsPic".indexOf(fileType)<0){
            fileType = "other";
        }
        String filePath = wxacodeUrl + File.separator + fileType;

        String fileName = StringUtils.date2Str(new Date(), StringUtils.LONG_DATE_TIME_FORMAT).concat(StringUtils.getNonceStr(6, true)) + StringUtils.getExtensionName(uploadedFile.getOriginalFilename());
        if (".png.jpeg.jpg".indexOf(StringUtils.getExtensionName(fileName.toLowerCase()))>=0){
            fileName = fileName.replace(StringUtils.getExtensionName(fileName), ".png");
        }

        RequestResult result = fileService.uploadFile(uploadedFile, filePath, fileName);

        return result;
    }

    @ApiOperation(value = "多个文件上传 | File")
    @PostMapping("/batch/uploadFile")
    public RequestResult handleFileUpload(@RequestParam(value = "fileType", required = false, defaultValue = "other") String fileType,
                                                 HttpServletRequest request){
        List<MultipartFile> files =((MultipartHttpServletRequest)request).getFiles("uploadingFile");
        RequestResult result;
        List<String> filePathList = new ArrayList<>();

        for (MultipartFile multipartFile : files){
            String filePath = wxacodeUrl + File.separator + fileType;

            String fileName = StringUtils.date2Str(new Date(), StringUtils.LONG_DATE_TIME_FORMAT).concat(StringUtils.getNonceStr(6, true)) + StringUtils.getExtensionName(multipartFile.getOriginalFilename());
            if (".png.jpeg.jpg".indexOf(StringUtils.getExtensionName(fileName.toLowerCase()))>=0){
                fileName = fileName.replace(StringUtils.getExtensionName(fileName), ".png");
            }

            result = fileService.uploadFile(multipartFile, filePath, fileName);
            if (!result.isSuccess()){
                return result;
            }else{
                filePathList.add(result.getData().toString());
            }
        }
        return RequestResult.success(filePathList);
    }


    @ApiOperation(value = "下载文件 | File")
    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam(value = "fileUrl", required = true, defaultValue = "") String fileUrl) throws Exception {

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        if("".equals(fileUrl) || fileUrl == null){
            return;
        }
        fileUrl.replace("\\",File.separator).replace("/",File.separator);
        String fullPath = wxacodeUrl + File.separator + fileUrl;
        // 读到流中
        InputStream inStream = new FileInputStream(fullPath);// 文件的存放路径
        OutputStream out = response.getOutputStream();
        //写文件
        int b;
        byte[] bytes = new byte[1024];
        while ((b = inStream.read(bytes)) > 0){
            out.write(bytes, 0, b);
        }
        inStream.close();
        out.close();
    }
}
