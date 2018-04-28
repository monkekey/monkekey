package com.yumi.butler.service;

import com.yumi.butler.domain.Sysuserrole;

import java.util.List;

/**
 * Created by zy on 2017/10/10.
 */
public interface SysUserRoleService {
    public List<Sysuserrole> findByUid(long uid);
}
