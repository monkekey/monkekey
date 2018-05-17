package com.hpd.butler.service;

import com.hpd.butler.vo.InnVo;

import java.util.List;

/**
 * Created by zy on 2018/1/3.
 */
public interface InnService {

    public List<InnVo> findValidInn();
}
