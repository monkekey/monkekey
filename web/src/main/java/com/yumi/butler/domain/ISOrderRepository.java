package com.yumi.butler.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

public interface ISOrderRepository extends JpaRepository<SOrder,Long> {

    public Page<SOrder> findByOrderStatusAndFlagAndOrderHotel(Pageable pageable,Integer status,Integer flag,String hotelID);

    public Page<SOrder> findByOrderStatusAndFlagAndOrderHotelAndOrderDeliveryby(Pageable pageable,Integer status,Integer flag,String hotelID,String username);

    public Page<SOrder> findByOrderStatusAndFlagAndOrderOpenidAndOrderCreateby(Pageable pageable,Integer status,Integer flag,String openid,String username);

    public Page<SOrder> findByOrderNumberContainingAndFlag(Pageable pageable,String orderNo, Integer flag);

    public  SOrder findByOrderNumberAndFlag(String orderNo,Integer flag);

    @Query(value = "SELECT s.`order_status`,COUNT(s.`id`) FROM s_order s WHERE s.`order_hotel`= ?1 AND s.`flag` = ?2 GROUP BY s.`order_status`", nativeQuery = true)
    public Object[] countOrder(String hotelId,Integer flag);

}
