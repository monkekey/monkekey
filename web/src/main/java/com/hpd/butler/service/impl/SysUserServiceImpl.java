package com.hpd.butler.service.impl;

import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.domain.*;
import com.hpd.butler.vo.UserVo;
import com.hpd.butler.service.SysUserService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2017/10/10.
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private ISysuserRepository iSysuserRepository;
    @Autowired
    private ISysuserdetailRepository iSysuserdetailRepository;
    @Autowired
    private IFavoriteRepository iFavoriteRepository;

    public Sysuser findByUsername(String username){
        return  iSysuserRepository.findByAccount(username);
    }


    public Sysuser findByUid(long uid){
        return  iSysuserRepository.findOne(uid);
    }

    public Sysuser updateById(Sysuser sysuser){
        return  iSysuserRepository.saveAndFlush(sysuser);
    }

    public List<UserVo> findAllUserByInncode(String inncode, String openid){
        List<UserVo> userVoList = new ArrayList<>();
        List<Long> uidList = new ArrayList<>();
        UserVo userVo = null;

        List<Sysuserdetail> sysuserdetailList = iSysuserdetailRepository.findByInnCode(inncode);
        for (Sysuserdetail sysuserdetail:sysuserdetailList){
            userVo = new UserVo();

            try {
                PropertyUtils.copyProperties(userVo, sysuserdetail);
                userVo.setFavoriteQty(0);
                userVo.setFavorited(0);
                userVoList.add(userVo);
                uidList.add(sysuserdetail.getUid());

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        if (uidList.size()>0) {
            List<Sysuser> sysuserList = iSysuserRepository.findByIdIn(uidList);
            for (Sysuser sysuser : sysuserList){
                for (UserVo uv: userVoList){
                    if (uv.getUid() == sysuser.getId()){
                        if (sysuser.getFlag() != CommonFlag.VALID.getValue()){
                            userVoList.remove(uv);
                        }else{
                            uv.setAccount(sysuser.getAccount());
                        }
                        break;
                    }
                }
            }
        }

        List<UserVo> favoriteCountlist = iFavoriteRepository.findFavoriteCount(inncode);
        for (UserVo uv: userVoList){
            for (UserVo qt : favoriteCountlist){
                if (qt.getAccount().equals(uv.getAccount()) && qt.getInnCode().equals(uv.getInnCode())){
                    uv.setFavoriteQty(qt.getFavoriteQty());
                    break;
                }
            }
        }

        List<UserVo> favoritedlist = iFavoriteRepository.findFavoriteCountByOpenid(inncode, openid);
        for (UserVo uv: userVoList){
            for (UserVo qt : favoritedlist){
                if (qt.getAccount().equals(uv.getAccount()) && qt.getInnCode().equals(uv.getInnCode())){
                    uv.setFavorited(1);
                    break;
                }
            }
        }

        return userVoList;
    }

    public UserVo findByAccount(String account){
        UserVo userVo = new UserVo();
        Sysuser sysuser = iSysuserRepository.findByAccount(account);
        if(null != sysuser){
            Sysuserdetail sysuserdetail = iSysuserdetailRepository.findByUid(sysuser.getId());
            try {
                PropertyUtils.copyProperties(userVo, sysuser);

                if(null != sysuserdetail){
                    PropertyUtils.copyProperties(userVo, sysuserdetail);
                    userVo.setId(sysuser.getId());

                }
                userVo.setPassword("");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return  userVo;
    }
}
