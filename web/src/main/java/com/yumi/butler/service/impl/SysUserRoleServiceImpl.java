package com.yumi.butler.service.impl;

import com.yumi.butler.domain.ISysuserroleRepository;
import com.yumi.butler.domain.Sysuserrole;
import com.yumi.butler.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zy on 2017/10/10.
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {
    @Autowired
    private ISysuserroleRepository iSysuserroleRepository;

    public List<Sysuserrole> findByUid(long uid){
        return iSysuserroleRepository.findByUid(uid);
    }
}
