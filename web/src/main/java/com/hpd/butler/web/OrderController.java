package com.hpd.butler.web;

import com.hpd.butler.bean.RefundResult;
import com.hpd.butler.common.RequestResult;
import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.domain.*;
import com.hpd.butler.service.WechatPayService;
import com.hpd.butler.utils.HeaderUtils;
import com.hpd.butler.utils.SnowFlake;
import com.hpd.butler.vo.IOrderVO;
import com.hpd.butler.vo.InnVo;
import com.hpd.butler.domain.IOrderRepository;
import com.hpd.butler.domain.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by zy on 2018/2/6.
 */
@RestController
@RequestMapping("/sys/order")
@Api(value = "OrderController", description = "订单相关接口")
public class OrderController {
    @Value("${bizcode.datacenterId}")
    private long datacenterId ;
    @Value("${bizcode.machineId}")
    private long machineId ;
    private SnowFlake snowFlake = null;

    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private ISysuserRepository iSysuserRepository;
    @Autowired
    private ISysuserdetailRepository iSysuserdetailRepository;
    @Autowired
    private ISOrderRepository isOrderRepository;
    @Autowired
    private ISOrderDetailRepository isOrderDetailRepository;
    @Autowired
    private ISOrderDao isOrderDao;
    @Autowired
    private IInnRepository innRepository;
    @Autowired
    private WechatPayService wechatPayService;


    @ApiOperation(value = "All Order|列表")
    @GetMapping("")
    public RequestResult getAll(@RequestParam(value = "innCode", required = false, defaultValue = "") final String innCode,
                                @RequestParam(value = "orderNo", required = false, defaultValue = "") final String orderNo,
                                @RequestParam(value = "beginDate", required = false, defaultValue = "") final String beginDate,
                                @RequestParam(value = "endDate", required = false, defaultValue = "") final String endDate,
                                @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        Specification<SOrder> spec = new Specification<SOrder>() {
            public Predicate toPredicate(Root<SOrder> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Predicate p = cb.equal(root.get("flag").as(Integer.class), CommonFlag.VALID.getValue());
                predicates.add(p);

                if(!StringUtils.isEmpty(beginDate)){
                    try {
                        p = cb.greaterThanOrEqualTo(root.get("orderCreatetime"), DateUtils.parseDate(beginDate+" 00:00:01", "yyyy-MM-dd HH:mm:ss"));
                        predicates.add(p);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(!StringUtils.isEmpty(endDate)){
                    try {
                        p = cb.lessThanOrEqualTo(root.get("orderCreatetime"), DateUtils.parseDate(endDate+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
                        predicates.add(p);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(!StringUtils.isEmpty(innCode)){
                    p = cb.equal(root.get("orderHotel").as(String.class), innCode);
                    predicates.add(p);
                }

                if(!StringUtils.isEmpty(orderNo)){
                    p = cb.equal(root.get("orderNumber").as(String.class), orderNo);
                    predicates.add(p);
                }

                Predicate[] props = new Predicate[predicates.size()];
                predicates.toArray(props);
                query.where(props);
                return query.getRestriction();
            }
        };

        List<InnVo> findHotel = innRepository.findAllInn();

        Page<SOrder> sysorder =  isOrderDao.findAll(spec,pageable);

        for (SOrder sOrder: sysorder.getContent()){
            for (InnVo innVo:findHotel){
                if (sOrder.getOrderHotel().equals(innVo.getCode())){
                    sOrder.setHotelName(innVo.getName());
                    break;
                }
            }
        }

        return RequestResult.success(sysorder);
    }

    @ApiOperation(value = "Order|指定订单列表")
    @GetMapping("/orderStatus")
    public RequestResult getOrderStatus(@RequestParam(value = "account", required = true) String account,
                                        @RequestParam(value = "status", required = true) Integer status,
                                        @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){

        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        Authentication currentuser=SecurityContextHolder.getContext().getAuthentication();
        if(currentuser==null){
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("未登录不可查询订单");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails currentAuth = (UserDetails)principal;

        Sysuser sysuser = iSysuserRepository.findByAccount(currentAuth.getUsername());
        if(null == sysuser || sysuser.getFlag() != CommonFlag.VALID.getValue()){
            return RequestResult.fail("此账户不存在或已禁用!");
        }
        Sysuserdetail sysuserdetail = iSysuserdetailRepository.findByUid(sysuser.getId());
        if(null == sysuserdetail){
            return RequestResult.fail("此账户暂未分配门店！");
        }
        Page<SOrder> orderPage = null;
        if(status == 0){
            orderPage = isOrderRepository.findByOrderStatusAndFlagAndOrderHotel(pageable,status,CommonFlag.VALID.getValue(),sysuserdetail.getInnCode());
        }else if(status == 1){
            orderPage = isOrderRepository.findByOrderStatusAndFlagAndOrderHotelAndOrderDeliveryby(pageable,status,CommonFlag.VALID.getValue(),sysuserdetail.getInnCode(),sysuser.getAccount());
        }else {
            orderPage = isOrderRepository.findByOrderStatusAndFlagAndOrderHotelAndOrderDeliveryby(pageable,status,CommonFlag.VALID.getValue(),sysuserdetail.getInnCode(),sysuser.getAccount());
        }

        if(orderPage == null){
            return RequestResult.fail("暂无订单");
        }

        return RequestResult.success(orderPage);
    }

    @ApiOperation(value = "Order|详情")
    @GetMapping("/{orderNo}")
    public RequestResult getOrder(@NotNull @PathVariable("orderNo") String orderNo) {
        IOrderVO orderVo = new IOrderVO();
        SOrder sOrder = isOrderRepository.findByOrderNumberAndFlag(orderNo,CommonFlag.VALID.getValue());

        if(null == sOrder){
            return RequestResult.fail("无法获取记录");
        }
        Inn inn = innRepository.findByInnCode(sOrder.getOrderHotel());
        sOrder.setHotelName(inn.getInnName());
        try {
            PropertyUtils.copyProperties(orderVo, sOrder);
            List<SOrderDetail> orderDetails = isOrderDetailRepository.findByOrderNumber(orderNo);
            if (null!=orderDetails){
                orderVo.setsOrderDetails(orderDetails);
            }
            return RequestResult.success(orderVo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return RequestResult.fail("获取详情失败");
    }

    @ApiOperation(value = "Order|统计订单")
    @GetMapping("/countOrder")
    public RequestResult getCountOrder(@RequestParam(value = "account", required = true) String account){
        Object[] objects = isOrderRepository.countOrder(account,CommonFlag.VALID.getValue());
        List<Map> maps = new ArrayList<>();
        Map<Integer,Integer> map = new HashMap<>();
        for(Object obj : objects){
            Object[] objs = (Object[]) obj;
            map.put(Integer.valueOf(objs[0].toString()),Integer.valueOf(objs[1].toString()));
        }
        return RequestResult.success(map);
    }

    @ApiOperation(value = "Order|查询订单编号")
    @GetMapping("/queryOrderNo")
    public RequestResult queryOrderNo(@RequestParam(value = "orderNo", required = true) String orderNo,
                                      @RequestParam(value = "pageIdx", required = true) Integer pageIdx,
                                      @RequestParam(value = "pageSize", required = true) Integer pageSize){
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");
        Page<SOrder> sOrder = isOrderRepository.findByOrderNumberContainingAndFlag(pageable,orderNo,CommonFlag.VALID.getValue());

        return RequestResult.success(sOrder);
    }

    @ApiOperation(value = "Order|订单退单")
    @RequestMapping(value = "/salesOrder", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult salesOrder(@RequestParam(value = "orderNo", required = true) String orderNo,
                                    @RequestParam(value = "salesRemark", required = true) String salesRemark){
        SOrder sOrder = isOrderRepository.findByOrderNumberAndFlag(orderNo,CommonFlag.VALID.getValue());
        if(sOrder == null){
            return RequestResult.fail("没有该订单");
        }
        RequestResult requestResult = wechatPayService.payRefund("Bee",sOrder.getOrderPaymentNumber(),sOrder.getOrderPaymentNumber()
                ,sOrder.getOrderSumPrice().toString(),sOrder.getOrderSumPrice().toString());
        System.out.println(requestResult.toString());

        if(null == requestResult.getData()){
            return RequestResult.fail("退款失败,请联系相关技术人员！！！");
        }
        RefundResult rfr = (RefundResult) requestResult.getData();
        if(!"SUCCESS".equals(rfr.getReturn_code())){
            return RequestResult.fail("退款失败,请联系相关技术人员！！！");
        }

        if(!"SUCCESS".equals(rfr.getResult_code())){
            return RequestResult.fail(rfr.getErr_code_des());
        }

        sOrder.setOrderRemark(salesRemark);
        sOrder.setOrderDeliverytime(new Date());
        sOrder.setOrderStatus(4);

        return RequestResult.success(rfr.getResult_code());
    }

    @ApiOperation(value = "新增|Order")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody Order order){
        //Authorization:Bearer + " " + token
        order.setOrderNo(generatorCode());
        order.setCreateBy(HeaderUtils.getCurrentUser());
        order.setLastModifyBy(HeaderUtils.getCurrentUser());
        order.setCreateTime(new Date());
        order.setLastModifyTime(new Date());
        order.setFlag(CommonFlag.VALID.getValue());

        order = orderRepository.saveAndFlush(order);
        return RequestResult.success(order);
    }

    @ApiOperation(value = "更新|Order")
    @RequestMapping(value = "/{orderNo}", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult update(@PathVariable("orderNo") String orderNo,
                                @RequestBody Order order){
        Order old_order = orderRepository.findByOrderNo(orderNo);

        if(null == old_order){
            return RequestResult.fail("无法获取记录");
        }

        old_order.setInnCode(order.getInnCode());
        old_order.setOrderNo("");
        old_order.setAmount(order.getAmount());
        old_order.setCreateBy(HeaderUtils.getCurrentUser());
        old_order.setCreateTime(new Date());
        old_order.setLastModifyBy(HeaderUtils.getCurrentUser());
        old_order.setLastModifyTime(new Date());

        old_order = orderRepository.saveAndFlush(old_order);
        return RequestResult.success(old_order);
    }

    @ApiOperation(value = "更新|OrderStatus")
    @RequestMapping(value = "/OrderStatus", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult updateStatus(@RequestParam(value = "orderNo", required = true) String orderNo,
                                      @RequestParam(value = "status", required = true) Integer status) {

        SOrder sOrder = isOrderRepository.findByOrderNumberAndFlag(orderNo,CommonFlag.VALID.getValue());
        if(null == sOrder){
            return RequestResult.fail("无法获取纪录");
        }
        sOrder.setOrderStatus(status);
        if(status == 0){
            sOrder.setOrderDeliveryby(HeaderUtils.getCurrentUser());
            sOrder.setOrderStatus(1);
        }
        if(status == 1){
            sOrder.setOrderDeliverytime(new Date());
            sOrder.setOrderStatus(2);
        }
        sOrder = isOrderRepository.saveAndFlush(sOrder);
        return RequestResult.success(sOrder);
    }


    @ApiOperation(value = "删除|Order")
    @DeleteMapping("")
    public RequestResult delete(@RequestHeader("Authorization") String Authorization,
                                @RequestParam(value = "orderNo", required = true) String orderNo,
                                @RequestParam(value = "delRemark", required = true) String delRemark) {
        Order order = orderRepository.findByOrderNo(orderNo);

        if(null == order){
            return RequestResult.fail("无法获取记录");
        }
        order.setRemark(String.format("【%s】%s", delRemark, order.getRemark()));
        //orderRepository.delete(order);
        order.setFlag(CommonFlag.DELETED.getValue());
        orderRepository.saveAndFlush(order);
        return  RequestResult.success("ok");
    }

    public String generatorCode(){
        if (null == snowFlake){
            snowFlake = new SnowFlake(datacenterId, machineId);
        }
        return "R".concat(Long.toString(snowFlake.nextId()));
    }
}
