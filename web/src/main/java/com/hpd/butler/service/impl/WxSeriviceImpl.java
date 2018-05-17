package com.hpd.butler.service.impl;

import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.domain.*;
import com.hpd.butler.service.RedisService;
import com.hpd.butler.service.WxSerivice;
import com.hpd.butler.vo.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "wxHotelCommodity")
public class WxSeriviceImpl implements WxSerivice {

    @Autowired
    private ISHotelCategoryRepository hotelCategoryRepository;
    @Autowired
    private ISCategoryRepository categoryRepository;
    @Autowired
    private ISHotelCommodityRepository hotelCommodityRepository;
    @Autowired
    private ISOrderRepository orderRepository;
    @Autowired
    private ISOrderDetailRepository orderDetailRepository;
    @Autowired
    private ISPaymentRepository paymentRepository;
    @Autowired
    private IInnRepository innRepository;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RedisTemplate redisTemplate;

    public List<SHotelCategory> hotelCategories(String hotelID){
        List<SHotelCategory> hotelCategoryList = hotelCategoryRepository.findByHotelIdAndFlag(hotelID, CommonFlag.VALID.getValue());
        List<SCategory> categoryList = categoryRepository.findByFlag(CommonFlag.VALID.getValue());
        for(SHotelCategory sHotelCategory : hotelCategoryList){
            for(SCategory sCategory : categoryList){
                if(sHotelCategory.getCategoryCode().equals(sCategory.getCategoryCode())){
                    sHotelCategory.setCategoryName(sCategory.getCategoryName());
                }
            }
        }
        return hotelCategoryList;
    }

    public List<CommodityVO> queryNewCommodity(String hotelId){

        Object[] objects = hotelCommodityRepository.queryNewCommodity(hotelId);
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
            commodityVO.setCommodityUnit(commodity[10].toString());
            hotelCommodityVOS.add(commodityVO);
        }

        return hotelCommodityVOS;

    }

    public List<CommodityVO> queryHotCommodity(String hotelId){

        Object[] objects = hotelCommodityRepository.queryHotCommodity(hotelId);
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
            commodityVO.setCommodityUnit(commodity[10].toString());
            hotelCommodityVOS.add(commodityVO);
        }

        return hotelCommodityVOS;

    }

    public List<CommodityVO> queryLikeCommodity(String hotelId){

        Object[] objects = hotelCommodityRepository.queryLikeCommodity(hotelId);
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
            commodityVO.setCommodityUnit(commodity[10].toString());
            hotelCommodityVOS.add(commodityVO);
        }

        return hotelCommodityVOS;

    }

    public List<CommodityVO> queryCommodity(String hotelId,String categoryCode,Integer pageIdx,Integer pageSize){

        Object[] objects = hotelCommodityRepository.queryCommodity(hotelId,categoryCode,pageIdx,pageSize);
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
            commodityVO.setCommodityUnit(commodity[10].toString());
            hotelCommodityVOS.add(commodityVO);
        }

        return hotelCommodityVOS;

    }

    public Page<SOrder> queryOrderList(String openid,String username,Integer status,Integer pageIdx,Integer pageSize){

        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");
        Page<SOrder> orderPage = orderRepository.findByOrderStatusAndFlagAndOrderOpenidAndOrderCreateby(pageable,status,CommonFlag.VALID.getValue(),openid,username);
        List<InnVo> findHotel = innRepository.findAllInn();
        for (SOrder sOrder: orderPage.getContent()){
            for (InnVo innVo:findHotel){
                if (sOrder.getOrderHotel().equals(innVo.getCode())){
                    sOrder.setHotelName(innVo.getName());
                    break;
                }
            }
        }

        return orderPage;
    }

    public IOrderVO queryOrderDateil(String orderNo){
        IOrderVO orderVo = new IOrderVO();
        SOrder sOrder = orderRepository.findByOrderNumberAndFlag(orderNo,CommonFlag.VALID.getValue());

        if(null == sOrder){
            return null;
        }
        Inn inn = innRepository.findByInnCode(sOrder.getOrderHotel());
        sOrder.setHotelName(inn.getInnName());
        try {
            PropertyUtils.copyProperties(orderVo, sOrder);
            List<SOrderDetail> orderDetails = orderDetailRepository.findByOrderNumber(orderNo);
            if (null!=orderDetails){
                orderVo.setsOrderDetails(orderDetails);
            }
            return orderVo;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public SOrder saveOrder(WxOrderVO wxOrderVO){

        String orderNumber = "ORDER-" + new Date().getTime();

        SOrder sOrder = new SOrder();

        SOrder new_sorder = new SOrder();

        sOrder.setOrderNumber(orderNumber);
        sOrder.setOrderCountNumber(wxOrderVO.getSum());
        sOrder.setOrderSumPrice(wxOrderVO.getPrice());
        sOrder.setOrderPaymentNumber(wxOrderVO.getPayNumber());
        sOrder.setOrderHotel(wxOrderVO.getHotelId());
        sOrder.setOrderRoom(wxOrderVO.getRoom());
        sOrder.setOrderCreateby(wxOrderVO.getCreateBy());
        sOrder.setOrderCreatetime(wxOrderVO.getCreateTime());
        sOrder.setOrderOpenid(wxOrderVO.getOpenId());
        sOrder.setOrderStatus(0);
        sOrder.setFlag(CommonFlag.VALID.getValue());
        new_sorder = orderRepository.saveAndFlush(sOrder);

        SPayment sPayment = new SPayment();
        sPayment.setPaymentNumber(wxOrderVO.getPayNumber());
        sPayment.setOpenid(wxOrderVO.getOpenId());
        sPayment.setPaymentCreateby(wxOrderVO.getCreateBy());
        sPayment.setPaymentCreatetime(wxOrderVO.getCreateTime());
        sPayment.setPaymentPrice(wxOrderVO.getPrice());
        sPayment.setPaymentStatus(1);
        sPayment.setFlag(CommonFlag.VALID.getValue());
        paymentRepository.saveAndFlush(sPayment);

        for(WxOrderDetailVO wxOrderDetailVO : wxOrderVO.getWxOrderDetailVOS()){
            SOrderDetail sOrderDetail = new SOrderDetail();
            sOrderDetail.setOrderNumber(orderNumber);
            sOrderDetail.setCategoryCode(wxOrderDetailVO.getCategoryCode());
            sOrderDetail.setCategoryName(wxOrderDetailVO.getCategoryName());
            sOrderDetail.setCommodityCode(wxOrderDetailVO.getCommodityCode());
            sOrderDetail.setCommodityName(wxOrderDetailVO.getCommodityName());
            sOrderDetail.setCommodityImg(wxOrderDetailVO.getCommodityImg());
            sOrderDetail.setCommodityPrice(wxOrderDetailVO.getCommodityPrice());
            sOrderDetail.setCommoditySumPrice(wxOrderDetailVO.getPirceSum());
            sOrderDetail.setCommodityAmount(wxOrderDetailVO.getCommoditySum());
            orderDetailRepository.saveAndFlush(sOrderDetail);

            SHotelCommodity sHotelCommodity = hotelCommodityRepository.findByCommodityCodeAndFlagAndHotelId(wxOrderDetailVO.getCommodityCode(),CommonFlag.VALID.getValue(),wxOrderVO.getHotelId());
            sHotelCommodity.setCommodityInventory(sHotelCommodity.getCommodityInventory() - wxOrderDetailVO.getCommoditySum());
            hotelCommodityRepository.saveAndFlush(sHotelCommodity);
        }

        return new_sorder;

    }

    public String checkStorage(WxOrderVO wxOrderVO){

        List<WxOrderDetailVO> orderDetailVOS = wxOrderVO.getWxOrderDetailVOS();
        for(WxOrderDetailVO wxOrderDetailVO : orderDetailVOS){
            String key = wxOrderVO.getHotelId()+"_"+wxOrderDetailVO.getCommodityCode();
            int num = redisService.get(key);
            if(num == 0){
                return wxOrderDetailVO.getCommodityName()+"商品已售完，请重新下单！";
            }else if(num >= 1){
                redisService.incrBy(key, -wxOrderDetailVO.getCommoditySum());
            }else if(num < wxOrderDetailVO.getCommoditySum()){
                return wxOrderDetailVO.getCommodityName()+"商品超出库存数量，请重新下单！";
            }
        }

        return "ok";
    }

    public String cleanOrder(WxOrderVO wxOrderVO){

        List<WxOrderDetailVO> orderDetailVOS = wxOrderVO.getWxOrderDetailVOS();
        for(WxOrderDetailVO wxOrderDetailVO : orderDetailVOS){
            String key = wxOrderVO.getHotelId()+"_"+wxOrderDetailVO.getCommodityCode();
            String sumKey = "sum_" + wxOrderVO.getHotelId()+"_"+wxOrderDetailVO.getCommodityCode();
            int num = redisService.get(key);
            int sumNum = redisService.get(sumKey);
            int count = num + Integer.valueOf(String.valueOf(wxOrderDetailVO.getCommoditySum()));
            if(sumNum > count){
                redisService.set(key,count);
            }else{
                redisService.set(key,sumNum);
            }

        }
        return "ok";
    }

}
