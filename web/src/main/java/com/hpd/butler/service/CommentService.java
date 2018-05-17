package com.hpd.butler.service;

import com.hpd.butler.vo.CommentVo;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

public interface CommentService {

    public String save(CommentVo commentVo) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    public BigDecimal count(String innCode, String customerServices);

}
