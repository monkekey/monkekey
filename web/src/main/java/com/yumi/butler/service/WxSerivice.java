package com.yumi.butler.service;

import com.yumi.butler.domain.SHotelCategory;
import com.yumi.butler.domain.SOrder;
import com.yumi.butler.vo.CommodityVO;
import com.yumi.butler.vo.IOrderVO;
import com.yumi.butler.vo.WxOrderVO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WxSerivice {

    public List<SHotelCategory> hotelCategories(String hotelID);

    public List<CommodityVO> queryNewCommodity(String hotelId);

    public List<CommodityVO> queryHotCommodity(String hotelId);

    public List<CommodityVO> queryLikeCommodity(String hotelId);

    public List<CommodityVO> queryCommodity(String hotelId,String categoryCode,Integer pageIdx,Integer pageSize);

    public Page<SOrder> queryOrderList(String hotelId, String username, Integer status, Integer pageIdx, Integer pageSize);

    public IOrderVO queryOrderDateil(String orderNo);

    public SOrder saveOrder(WxOrderVO wxOrderVO);

    public String checkStorage(WxOrderVO wxOrderVO);

    public String cleanOrder(WxOrderVO wxOrderVO);

}
