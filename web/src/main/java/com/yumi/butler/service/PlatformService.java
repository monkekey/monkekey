package com.yumi.butler.service;

import com.yumi.butler.common.RequestResult;
import org.springframework.web.multipart.MultipartFile;

public interface PlatformService {

    public RequestResult getIDCard(MultipartFile uploadedFile, String orderNo) throws Exception;

    public RequestResult getFace(MultipartFile uploadedFile, String IDurl, String orderNo) throws Exception;

}
