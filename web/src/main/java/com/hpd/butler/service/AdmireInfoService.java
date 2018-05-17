package com.hpd.butler.service;

import com.hpd.butler.domain.AdmireInfo;

public interface AdmireInfoService {

    public void save(AdmireInfo admireInfo);

    public int count(String innCode, String name);

}
