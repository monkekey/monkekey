package com.yumi.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zy on 2017/10/9.
 */
public interface IWechattokenRepository extends JpaRepository<Wechattoken, Long> {

    public Wechattoken findByAccountIdAndFlag(Long accountId, Integer flag);
}
