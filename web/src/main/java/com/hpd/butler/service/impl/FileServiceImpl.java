package com.hpd.butler.service.impl;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.service.FileService;
import com.hpd.butler.utils.ImageZipUtil;
import com.hpd.butler.utils.VoiceUtils;
import com.hpd.netty.tools.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created on 2017/10/11.
 */
@Service
public class FileServiceImpl implements FileService {
    @Value("${silk-v3-decoder}")
    private String silkv3decoder;

    /**
     * 单文件上传
     * @param uploadedFile
     * @param filePath
     * @return
     */
    public RequestResult uploadFile(MultipartFile uploadedFile, String filePath, String fileName) {
        RequestResult result = saveFile(uploadedFile, filePath, fileName);
        return result;
    }

    /**
     * 多文件上传
     * @param uploadedFiles
     * @param filePaths
     * @return
     */
    public RequestResult uploadFiles(MultipartFile[] uploadedFiles, String[] filePaths, String[] fileNames){

        for(int i = 0;i<uploadedFiles.length;i++){
            MultipartFile uploadedFile = uploadedFiles[i];
            RequestResult result = saveFile(uploadedFile, filePaths[i],fileNames[i]);
            if(!result.isSuccess()){
                String errorMsg = "文件【" + uploadedFile.getOriginalFilename() + "】上传发送错误";
                System.out.println(errorMsg);
                return RequestResult.fail(errorMsg);
            }
        }
        return RequestResult.success("");
    }


    public RequestResult saveFile(MultipartFile uploadedFile, String filePath, String fileName){
        try {
            if(uploadedFile != null){
                File uploadFold = new File(filePath);
                if (!uploadFold.exists()) {
                    uploadFold.mkdirs();
                }
                String fullPath = filePath + File.separator + fileName;
                File uploadFile = new File(fullPath);
                if (!uploadFile.exists()) {
                    uploadFile.createNewFile();
                }
                uploadedFile.transferTo(new File(fullPath));
                if (fileName.toLowerCase().endsWith("silk")){
                    //FileUtils.changeSilk(fullPath);
                    boolean flag = VoiceUtils.silk2mp3(fullPath, silkv3decoder);
                    if (flag){
                        return RequestResult.success(fileName.replace(".silk", ".mp3"));
                    }else {
                        return RequestResult.success(fileName);
                    }
                }else if(fileName.toLowerCase().endsWith(".png") && uploadedFile.getSize() > 2048000){
                    String newFullPath = filePath + File.separator + fileName.replace(".", "YS.");
                    String target = ImageZipUtil.zipImageFile(new File(fullPath), new File(newFullPath), 400, 0, 1);
                    return RequestResult.success(StringUtils.isNULL(target) ? fileName : fileName.replace(".", "YS."));
                }else{
                    return RequestResult.success(fileName);
                }
            } else {
                return RequestResult.fail("文件不能为空!");
            }
        } catch (IOException ce){
            ce.printStackTrace();
            return RequestResult.fail(ce.getMessage());
        }
    }
}
