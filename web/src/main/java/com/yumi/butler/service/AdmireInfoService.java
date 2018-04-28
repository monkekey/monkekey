package com.yumi.butler.service;

import com.yumi.butler.domain.AdmireInfo;

public interface AdmireInfoService {

    public void save(AdmireInfo admireInfo);

    public int count(String innCode, String name);

}
