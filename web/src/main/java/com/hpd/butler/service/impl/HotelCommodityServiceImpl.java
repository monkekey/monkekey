package com.hpd.butler.service.impl;

import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.domain.ISHotelCategoryRepository;
import com.hpd.butler.domain.ISHotelCommodityRepository;
import com.hpd.butler.domain.SHotelCategory;
import com.hpd.butler.domain.SHotelCommodity;
import com.hpd.butler.service.HotelCommodityService;
import com.hpd.butler.service.RedisService;
import com.hpd.butler.utils.RedisUtils;
import com.hpd.butler.vo.CommodityVO;
import com.hpd.butler.vo.HotelCommodityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HotelCommodityServiceImpl implements HotelCommodityService {

    @Autowired
    private ISHotelCategoryRepository hotelCategoryRepository;
    @Autowired
    private ISHotelCommodityRepository hotelCommodityRepository;
    @Autowired
    @Qualifier("entityManagerFactoryButler")
    private EntityManager entityManager;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedisService redisService;

//    Jedis jedis = new Jedis("localhost");

    public List<CommodityVO> queryHotelCommodity(Integer pageIdx, Integer pageSize, String hotelID, String keyword){
        Object[] objects = hotelCommodityRepository.queryHotelAndCommodityName(pageIdx,pageSize,hotelID,keyword);
        Object[] count = hotelCommodityRepository.count(hotelID,keyword);
        List<CommodityVO> hotelCommodityVOS = new ArrayList<CommodityVO>();
        for(Object objs : objects){
            Object[] commodity = (Object[]) objs;
            CommodityVO commodityVO = new CommodityVO();
            commodityVO.setId(Long.valueOf(commodity[0].toString()));
            commodityVO.setCategoryName(commodity[1].toString());
            commodityVO.setCommodityName(commodity[2].toString());
            commodityVO.setCommodityImg(commodity[3].toString());
            commodityVO.setCommoditySynopsis(commodity[4].toString());
            commodityVO.setCommodityPrice(new BigDecimal(commodity[5]+""));
            commodityVO.setCommodityCost(new BigDecimal(commodity[6]+""));
            commodityVO.setCommodityInventory(Integer.valueOf(commodity[7]+""));
            commodityVO.setCommodityCode(commodity[8].toString());
            commodityVO.setCategoryCode(commodity[9].toString());
            commodityVO.setTotalCount(Integer.valueOf(count[0]+""));
            hotelCommodityVOS.add(commodityVO);
        }

        return hotelCommodityVOS;
    }

    @Override
    @Transactional
    public String save(HotelCommodityVO hotelCommodityVO,String createBy) {
        String hotelId = hotelCommodityVO.getHotelId();

        SHotelCategory old_sHotelCategory = new SHotelCategory();
        old_sHotelCategory = hotelCategoryRepository.findByHotelIdAndCategoryCode(hotelId,hotelCommodityVO.getHotelCategoryList());
        if(old_sHotelCategory == null){
            SHotelCategory sHotelCategory = new SHotelCategory();
            sHotelCategory.setHotelId(hotelId);
            sHotelCategory.setCategoryCode(hotelCommodityVO.getHotelCategoryList());
            sHotelCategory.setFlag(CommonFlag.VALID.getValue());
            hotelCategoryRepository.saveAndFlush(sHotelCategory);
        }

        //循环保存商品
        List<SHotelCommodity> hotelCommodityList = new ArrayList<SHotelCommodity>();
        for (int j = 0; j < hotelCommodityVO.getHotelCommoditylist().size(); j++){
            SHotelCommodity sHotelCommodity = new SHotelCommodity();
            sHotelCommodity.setHotelId(hotelId);
            sHotelCommodity.setCategoryCode(hotelCommodityVO.getHotelCommoditylist().get(j).getCategoryCode());
            sHotelCommodity.setCommodityCode(hotelCommodityVO.getHotelCommoditylist().get(j).getCommodityCode());
            sHotelCommodity.setCommodityInventory(hotelCommodityVO.getHotelCommoditylist().get(j).getCommodityInventory());
            sHotelCommodity.setCreateBy(createBy);
            sHotelCommodity.setCreateTime(new Date());
            sHotelCommodity.setLastBy(createBy);
            sHotelCommodity.setLastTime(new Date());
            sHotelCommodity.setFlag(CommonFlag.VALID.getValue());
            hotelCommodityList.add(sHotelCommodity);
        }



        SHotelCommodity new_sHotelCommodity = null;
        for (int i = 0; i < hotelCommodityList.size(); i++) {
            new_sHotelCommodity = hotelCommodityList.get(i);
            hotelCommodityRepository.saveAndFlush(new_sHotelCommodity);
        }

        return "ok";
    }

    public List<CommodityVO> queryHotel(String hotelID){
        Object[] objects = hotelCommodityRepository.queryHotel(hotelID);
        List<CommodityVO> hotelCommodityVOS = new ArrayList<CommodityVO>();
        for(Object objs : objects){
            Object[] commodity = (Object[]) objs;
            CommodityVO commodityVO = new CommodityVO();
            commodityVO.setId(Long.valueOf(commodity[0].toString()));
            commodityVO.setCategoryName(commodity[1].toString());
            commodityVO.setCommodityName(commodity[2].toString());
            commodityVO.setCommodityImg(commodity[3].toString());
            commodityVO.setCommoditySynopsis(commodity[4].toString());
            commodityVO.setCommodityPrice(new BigDecimal(commodity[5]+""));
            commodityVO.setCommodityCost(new BigDecimal(commodity[6]+""));
            commodityVO.setCommodityInventory(Integer.valueOf(commodity[7]+""));
            commodityVO.setCommodityCode(commodity[8].toString());
            commodityVO.setCategoryCode(commodity[9].toString());
            hotelCommodityVOS.add(commodityVO);
        }

        return hotelCommodityVOS;
    }

    public String update(SHotelCommodity sHotelCommodity,String username){
        SHotelCommodity new_sHotelCommodity = hotelCommodityRepository.findOne(sHotelCommodity.getId());
        if(sHotelCommodity == null){
            return "该门店下无此商品";
        }
        new_sHotelCommodity.setCommodityInventory(sHotelCommodity.getCommodityInventory());
        new_sHotelCommodity.setLastBy(username);
        new_sHotelCommodity.setLastTime(new Date());
        new_sHotelCommodity = hotelCommodityRepository.saveAndFlush(new_sHotelCommodity);
        //将商品库存存储到Redis中，命名方式以门店id与商品的code
        redisService.set(new_sHotelCommodity.getHotelId()+"_"+ new_sHotelCommodity.getCommodityCode(),Integer.parseInt(String.valueOf(new_sHotelCommodity.getCommodityInventory())));//消耗库存
        redisService.set("sum_"+new_sHotelCommodity.getHotelId()+"_"+ new_sHotelCommodity.getCommodityCode(),Integer.parseInt(String.valueOf(new_sHotelCommodity.getCommodityInventory())));//总库存
//        jedis.set(new_sHotelCommodity.getHotelId()+"_"+ new_sHotelCommodity.getCommodityCode(),new_sHotelCommodity.getCommodityInventory().toString());//消耗库存
//        jedis.set("sum_"+new_sHotelCommodity.getHotelId()+"_"+ new_sHotelCommodity.getCommodityCode(),new_sHotelCommodity.getCommodityInventory().toString());//总库存
        System.out.println(new_sHotelCommodity.getHotelId()+"_"+ new_sHotelCommodity.getCommodityCode());
        return "ok";
    }

    public String del(Long id,String username){
        SHotelCommodity sHotelCommodity = new SHotelCommodity();
        sHotelCommodity = hotelCommodityRepository.findOne(id);
        if(sHotelCommodity == null){
            return "该门店下无此商品";
        }
        sHotelCommodity.setFlag(CommonFlag.DELETED.getValue());
        sHotelCommodity.setLastBy(username);
        sHotelCommodity.setLastTime(new Date());
        hotelCommodityRepository.saveAndFlush(sHotelCommodity);
        return "ok";
    }

}
