package com.yumi.butler.service;

import com.yumi.butler.domain.Sysuser;
import com.yumi.butler.vo.UserVo;

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
