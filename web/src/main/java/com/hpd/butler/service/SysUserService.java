package com.hpd.butler.service;

import com.hpd.butler.domain.Sysuser;
import com.hpd.butler.vo.UserVo;

import java.util.List;

/**
 * Created by zy on 2017/10/10.
 */

public interface SysUserService {
    public Sysuser findByUsername(String username);

    public Sysuser findByUid(long uid);

    public Sysuser updateById(Sysuser sysuser);

    public List<UserVo> findAllUserByInncode(String inncode, String openid);

    public UserVo findByAccount(String account);
}
