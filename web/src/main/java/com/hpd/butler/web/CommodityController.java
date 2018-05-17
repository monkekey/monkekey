package com.hpd.butler.web;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.domain.*;
import com.hpd.butler.service.HotelCommodityService;
import com.hpd.butler.utils.HeaderUtils;
import com.hpd.butler.vo.CommodityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zy on 2018/2/6.
 */
@RestController
@RequestMapping("/commodity/dict")
@Api(value = "CommodityController", description = "商品相关接口")
public class CommodityController {

    @Autowired
    private ISCommodityRepository isCommodityRepository;
    @Autowired
    private ISCommodityDao isCommodityDao;
    @Autowired
    private ISCategoryRepository isCategoryRepository;
    @Autowired
    private HotelCommodityService hotelCommodityService;

    @ApiOperation(value = "Commodity|列表")
    @GetMapping("")
    public RequestResult getAll(@RequestParam(value = "typeCode", required = false, defaultValue = "") final String typeCode,
                                @RequestParam(value = "itemName", required = false, defaultValue = "") final String itemName,
                                @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        Specification<SCommodity> spec = new Specification<SCommodity>() {
            public Predicate toPredicate(Root<SCommodity> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Predicate p = cb.equal(root.get("flag").as(Integer.class), CommonFlag.VALID.getValue());
                predicates.add(p);

                if(!StringUtils.isEmpty(typeCode)){
                    p = cb.equal(root.get("commodityCode").as(String.class), typeCode);
                    predicates.add(p);
                }

                if(!StringUtils.isEmpty(itemName)){
                    p = cb.like(root.get("commodityName").as(String.class), "%"+itemName+"%");
                    predicates.add(p);
                }

                Predicate[] props = new Predicate[predicates.size()];
                predicates.toArray(props);
                query.where(props);
                return query.getRestriction();
            }
        };

        List<SCategory> sCategoryList = isCategoryRepository.findByFlag(CommonFlag.VALID.getValue());

        Page<SCommodity> commodityPage =  isCommodityDao.findAll(spec,pageable);
        for (SCommodity sCommodity: commodityPage.getContent()){
            for (SCategory SCategory:sCategoryList){
                if (sCommodity.getCommodityCategoryCode().equals(SCategory.getCategoryCode())){
                    sCommodity.setCategoryName(SCategory.getCategoryName());
                    break;
                }
            }
        }

        return RequestResult.success(commodityPage);
    }

    @ApiOperation(value = "All Commodity|列表")
    @GetMapping("/all")
    public RequestResult getAllDictType(){
        return RequestResult.success(isCommodityRepository.findByFlagOrderByCommodityCode(CommonFlag.VALID.getValue()));
    }

    @ApiOperation(value = "Commodity|详情")
    @GetMapping("/{did}")
    public RequestResult getDict(@NotNull @PathVariable("did") long did) {
        SCommodity sCommodity = isCommodityRepository.findOne(did);

        if(null == sCommodity){
            return RequestResult.fail("无法获取记录");
        }
        return RequestResult.success(sCommodity);
    }

    @ApiOperation(value = "IN Commodity|列表")
    @GetMapping("/in")
    public RequestResult getList(@RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "categoryCodes", required = true) List<String> categoryCode) {
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.ASC, "commodityCategoryCode");

        List<SCategory> sCategoryList = isCategoryRepository.findByFlag(CommonFlag.VALID.getValue());
        Page<SCommodity> commodityPage = isCommodityRepository.findAllByCommodityCategoryCodeInAndFlag(pageable,categoryCode,CommonFlag.VALID.getValue());

        if(null == commodityPage){
            return RequestResult.fail("无法获取记录");
        }

        for (SCommodity sCommodity: commodityPage.getContent()){
            for (SCategory SCategory:sCategoryList){
                if (sCommodity.getCommodityCategoryCode().equals(SCategory.getCategoryCode())){
                    sCommodity.setCategoryName(SCategory.getCategoryName());
                    break;
                }
            }
        }

        return RequestResult.success(commodityPage);
    }

    @ApiOperation(value = "hotel Commodity|列表")
    @GetMapping("/hotelCommodity")
    public RequestResult gethotelCommodity(@RequestParam(value = "hotelID", required = false) String hotelID) {

        List<CommodityVO> hotelCommodityVOS = new ArrayList<CommodityVO>();
        hotelCommodityVOS = hotelCommodityService.queryHotel(hotelID);

        return RequestResult.success(hotelCommodityVOS);
    }

    @ApiOperation(value = "新增|Commodity")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody SCommodity sCommodity){
        //Authorization:Bearer + " " + token
        sCommodity.setCommodityCode("CD" + new Date().getTime());
        sCommodity.setCommodityInventory(1L);
        sCommodity.setCommodityCost(new BigDecimal(new DecimalFormat("#,##0.00").format(sCommodity.getCommodityCost())));
        sCommodity.setCommodityPrice(new BigDecimal(new DecimalFormat("#,##0.00").format(sCommodity.getCommodityPrice())));
        sCommodity.setCommodityCreateby(HeaderUtils.getCurrentUser());
        sCommodity.setCommodityCreatetime(new Date());
        sCommodity.setCommodityLastby(HeaderUtils.getCurrentUser());
        sCommodity.setCommodityLasttime(new Date());
        sCommodity.setFlag(CommonFlag.VALID.getValue());
        sCommodity = isCommodityRepository.saveAndFlush(sCommodity);
        return RequestResult.success(sCommodity);
    }

    @ApiOperation(value = "更新|Commodity")
    @RequestMapping(value = "/{dtid}", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult update(@PathVariable("dtid") long dtid,
                                @RequestBody SCommodity sCommodity){
        SCommodity old_scommodity = isCommodityRepository.findOne(dtid);

        if(null == old_scommodity){
            return RequestResult.fail("无法获取记录");
        }

        old_scommodity.setCommodityCategoryCode(sCommodity.getCommodityCategoryCode());
        old_scommodity.setCommodityName(sCommodity.getCommodityName());
        old_scommodity.setCommoditySynopsis(sCommodity.getCommoditySynopsis());
        old_scommodity.setCommodityInventory(sCommodity.getCommodityInventory());
        old_scommodity.setCommodityUnit(sCommodity.getCommodityUnit());
        old_scommodity.setCommodityCost(sCommodity.getCommodityCost());
        old_scommodity.setCommodityPrice(sCommodity.getCommodityPrice());
        old_scommodity.setCommodityLastby(HeaderUtils.getCurrentUser());
        old_scommodity.setCommodityLasttime(new Date());

        old_scommodity = isCommodityRepository.saveAndFlush(old_scommodity);
        return RequestResult.success(old_scommodity);
    }

    @ApiOperation(value = "删除|Commodity")
    @DeleteMapping("")
    public RequestResult delete(@RequestHeader("Authorization") String Authorization,
                                @RequestParam(value = "id", required = true)  long dtid) {
        SCommodity sCommodity = isCommodityRepository.findOne(dtid);

        if(null == sCommodity){
            return RequestResult.fail("无法获取记录");
        }
        //sysDictRepository.delete(sysDict);
        sCommodity.setFlag(CommonFlag.DELETED.getValue());
        isCommodityRepository.saveAndFlush(sCommodity);
        return  RequestResult.success("ok");
    }
}
