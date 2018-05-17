package com.hpd.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zy on 2018/2/6.
 */
public interface IOrderRepository extends JpaRepository<Order, Long> {
    public Order findByOrderNo(String orderNo);
}
