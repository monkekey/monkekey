package com.hpd.butler.domain;

import com.hpd.butler.vo.OrderDetailVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by zy on 2018/2/6.
 */
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("select new com.hpd.butler.vo.OrderDetailVo(od.orderNo, od.goodsId, g.goodsName, od.qty, od.salePrice) from OrderDetail od ,Goods g  where od.orderNo = :orderNo and od.goodsId = g.id")
//    @Query(value = "select * from g_order_detail od inner join goods g on od.goodsId = g.id ", nativeQuery = true)
    public List<OrderDetailVo> findByOrderNo(@Param("orderNo")String orderNo);
}
