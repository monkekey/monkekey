package com.yumi.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ISHotelCommodityRepository extends JpaRepository<SHotelCommodity,Long> {

    public SHotelCommodity findByCommodityCodeAndFlagAndHotelId(String commodityCode,Integer flag,String hotelId);

    public Page<SHotelCommodity> findByHotelIdAndFlagOrderByCommodityInventory(Pageable pageable,String hotelID,int flag);

    @Query(value = "SELECT s.id, c.`category_name`,d.`commodity_name`,d.`commodity_img`,d.commodity_synopsis,d.commodity_price,d.commodity_cost, s.`commodity_inventory`,s.`commodity_code`,s.`category_code`,d.`commodity_unit` " +
            "FROM s_hotel_commodity s " + "LEFT JOIN s_commodity d ON s.`commodity_code` = d.`commodity_code` " +
            "LEFT JOIN s_category c ON c.`category_code` = s.`category_code` " +
            "WHERE s.hotel_id= ?3" + " AND s.flag=1 AND d.commodity_name LIKE CONCAT('%', ?4 ,'%') LIMIT ?1,?2"
            ,nativeQuery = true)
    public Object[] queryHotelAndCommodityName(Integer pageIdx,Integer pageSize,String hotelID,String keyword);

    @Query(value = "SELECT COUNT(s.id)" +
            "FROM s_hotel_commodity s " + "LEFT JOIN s_commodity d ON s.`commodity_code` = d.`commodity_code` " +
            "LEFT JOIN s_category c ON c.`category_code` = s.`category_code` " +
            "WHERE s.hotel_id= ?1" + " AND s.flag=1 AND d.commodity_name LIKE CONCAT('%', ?2 ,'%')"
            ,nativeQuery = true)
    public Object[] count(String hotelID,String keyword);

    @Query(value = "SELECT s.id, c.`category_name`,d.`commodity_name`,d.`commodity_img`,d.commodity_synopsis,d.commodity_price,d.commodity_cost, s.`commodity_inventory`,s.`commodity_code`,s.`category_code`,d.`commodity_unit` " +
            "FROM s_hotel_commodity s " + "LEFT JOIN s_commodity d ON s.`commodity_code` = d.`commodity_code` " +
            "LEFT JOIN s_category c ON c.`category_code` = s.`category_code` " +
            "WHERE s.hotel_id= ?1" + " AND s.flag=1 "
            ,nativeQuery = true)
    public Object[] queryHotel(String hotelID);

    @Query(value = "SELECT s.id, c.`category_name`,d.`commodity_name`,d.`commodity_img`,d.commodity_synopsis,d.commodity_price,d.commodity_cost, s.`commodity_inventory`,s.`commodity_code`,s.`category_code`,d.`commodity_unit` " +
            "FROM s_hotel_commodity s " + "LEFT JOIN s_commodity d ON s.`commodity_code` = d.`commodity_code` " +
            "LEFT JOIN s_category c ON c.`category_code` = s.`category_code` " +
            "WHERE s.hotel_id= ?1" + " AND s.flag=1 ORDER BY s.create_time LIMIT 0,4"
            ,nativeQuery = true)
    public Object[] queryNewCommodity(String hotelID);

    @Query(value = "SELECT s.id, c.`category_name`,d.`commodity_name`,d.`commodity_img`,d.commodity_synopsis,d.commodity_price,d.commodity_cost, s.`commodity_inventory`,s.`commodity_code`,s.`category_code`,d.`commodity_unit` " +
            "FROM s_hotel_commodity s " + "LEFT JOIN s_commodity d ON s.`commodity_code` = d.`commodity_code` " +
            "LEFT JOIN s_category c ON c.`category_code` = s.`category_code` " +
            "WHERE s.hotel_id= ?1" + " AND s.flag=1 ORDER BY s.create_time LIMIT 5,4"
            ,nativeQuery = true)
    public Object[] queryHotCommodity(String hotelID);

    @Query(value = "SELECT s.id, c.`category_name`,d.`commodity_name`,d.`commodity_img`,d.commodity_synopsis,d.commodity_price,d.commodity_cost, s.`commodity_inventory`,s.`commodity_code`,s.`category_code`,d.`commodity_unit` " +
            "FROM s_hotel_commodity s " + "LEFT JOIN s_commodity d ON s.`commodity_code` = d.`commodity_code` " +
            "LEFT JOIN s_category c ON c.`category_code` = s.`category_code` " +
            "WHERE s.hotel_id= ?1" + " AND s.flag=1 ORDER BY s.create_time LIMIT 9,4"
            ,nativeQuery = true)
    public Object[] queryLikeCommodity(String hotelID);

    @Query(value = "SELECT s.id, c.`category_name`,d.`commodity_name`,d.`commodity_img`,d.commodity_synopsis,d.commodity_price,d.commodity_cost, s.`commodity_inventory`,s.`commodity_code`,s.`category_code`,d.`commodity_unit` " +
            "FROM s_hotel_commodity s " + "LEFT JOIN s_commodity d ON s.`commodity_code` = d.`commodity_code` " +
            "LEFT JOIN s_category c ON c.`category_code` = s.`category_code` " +
            "WHERE s.hotel_id= ?1" + " AND s.flag=1 AND s.`category_code`= ?2 ORDER BY s.id LIMIT ?3,?4"
            ,nativeQuery = true)
    public Object[] queryCommodity(String hotelID,String categoryCode,Integer pageIdx,Integer pageSize);

}
