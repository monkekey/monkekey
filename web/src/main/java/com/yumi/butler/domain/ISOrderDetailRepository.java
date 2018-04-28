package com.yumi.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISOrderDetailRepository extends JpaRepository<SOrderDetail,Long> {

    public List<SOrderDetail> findByOrderNumber(String orderNum);

}
