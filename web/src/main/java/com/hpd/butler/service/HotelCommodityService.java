package com.hpd.butler.service;

import com.hpd.butler.domain.SHotelCommodity;
import com.hpd.butler.vo.CommodityVO;
import com.hpd.butler.vo.HotelCommodityVO;

import java.util.List;

public interface HotelCommodityService {

    public List<CommodityVO> queryHotelCommodity(Integer pageIdx, Integer pageSize, String hotelID, String keyword);

    public String save(HotelCommodityVO hotelCommodityVO,String createBy);

    public List<CommodityVO> queryHotel(String hotelId);

    public String update(SHotelCommodity sHotelCommodity,String username);

    public String del(Long id,String username);

}
