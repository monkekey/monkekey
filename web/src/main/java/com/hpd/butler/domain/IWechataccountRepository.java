package com.hpd.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zy on 2017/10/9.
 */
public interface IWechataccountRepository extends JpaRepository<Wechataccount, Long> {

    public Wechataccount findByPlatformAndFlag(String platform, Integer flag);
}
