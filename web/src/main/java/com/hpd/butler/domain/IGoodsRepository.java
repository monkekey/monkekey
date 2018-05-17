package com.hpd.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zy on 2018/2/6.
 */
public interface IGoodsRepository extends JpaRepository<Goods, Long> {

    public Page<Goods> findByFlag(Pageable pageable, Integer flag);
}
