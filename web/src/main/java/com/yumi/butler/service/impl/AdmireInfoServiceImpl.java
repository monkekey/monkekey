package com.yumi.butler.service.impl;

import com.yumi.butler.domain.AdmireInfo;
import com.yumi.butler.domain.IAdmireInfoRepository;
import com.yumi.butler.service.AdmireInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdmireInfoServiceImpl implements AdmireInfoService {

    @Autowired
    private IAdmireInfoRepository iAdmireInfoRepository;

    public void save(AdmireInfo admireInfo){
        iAdmireInfoRepository.saveAndFlush(admireInfo);
    }

    public int count(String innCode, String name) {

        Object[] objects = iAdmireInfoRepository.countComment(innCode,name);
        BigDecimal countBig = new BigDecimal(objects[0] + "");
        int count = countBig.intValue();
        return count;
    }

}
