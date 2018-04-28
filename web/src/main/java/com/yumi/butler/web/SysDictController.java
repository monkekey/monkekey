package com.yumi.butler.web;

import com.yumi.butler.common.RequestResult;
import com.yumi.butler.constant.CommonFlag;
import com.yumi.butler.domain.*;
import com.yumi.butler.domain.*;
import com.yumi.butler.utils.HeaderUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zy on 2018/2/6.
 */
@RestController
@RequestMapping("/sys/dict")
@Api(value = "SysDictController", description = "字典相关接口")
public class SysDictController {
    @Autowired
    private ISysDictRepository sysDictRepository;
    @Autowired
    private ISysDictTypeRepository sysDictTypeRepository;
    @Autowired
    private ISysDictDao sysDictDao;

    @ApiOperation(value = "SysDict|列表")
    @GetMapping("")
    public RequestResult getAll(@RequestParam(value = "typeCode", required = false, defaultValue = "") final String typeCode,
                                @RequestParam(value = "itemName", required = false, defaultValue = "") final String itemName,
                                @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        Specification<SysDict> spec = new Specification<SysDict>() {
            public Predicate toPredicate(Root<SysDict> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                Predicate p = cb.equal(root.get("flag").as(Integer.class), CommonFlag.VALID.getValue());
                predicates.add(p);

                if(!StringUtils.isEmpty(typeCode)){
                    p = cb.equal(root.get("typeCode").as(String.class), typeCode);
                    predicates.add(p);
                }

                if(!StringUtils.isEmpty(itemName)){
                    p = cb.like(root.get("itemName").as(String.class), "%"+itemName+"%");
                    predicates.add(p);
                }

                Predicate[] props = new Predicate[predicates.size()];
                predicates.toArray(props);
                query.where(props);
                return query.getRestriction();
            }
        };

        List<SysDictType> sysDictTypeList = sysDictTypeRepository.findByFlag(CommonFlag.VALID.getValue());

        Page<SysDict> sysDictPage =  sysDictDao.findAll(spec,pageable);
        for (SysDict sysDict: sysDictPage.getContent()){
            for (SysDictType sysDictType:sysDictTypeList){
                if (sysDict.getTypeCode().equals(sysDictType.getTypeCode())){
                    sysDict.setTypeName(sysDictType.getTypeName());
                    break;
                }
            }
        }

        return RequestResult.success(sysDictPage);
    }

    @ApiOperation(value = "All SysDict|列表")
    @GetMapping("/all")
    public RequestResult getAllDictType(){
        return RequestResult.success(sysDictRepository.findByFlagOrderByTypeCode(CommonFlag.VALID.getValue()));
    }

    @ApiOperation(value = "SysDict|详情")
    @GetMapping("/{did}")
    public RequestResult getDict(@NotNull @PathVariable("did") long did) {
        SysDict sysDict = sysDictRepository.findOne(did);

        if(null == sysDict){
            return RequestResult.fail("无法获取记录");
        }
        return RequestResult.success(sysDict);
    }

    @ApiOperation(value = "新增|SysDict")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody SysDict sysDict){
        //Authorization:Bearer + " " + token
        sysDict.setCreateBy(HeaderUtils.getCurrentUser());
        sysDict.setLastModifyBy(HeaderUtils.getCurrentUser());
        sysDict.setCreateTime(new Date());
        sysDict.setLastModifyTime(new Date());
        sysDict.setFlag(CommonFlag.VALID.getValue());

        sysDict = sysDictRepository.saveAndFlush(sysDict);
        return RequestResult.success(sysDict);
    }

    @ApiOperation(value = "更新|SysDict")
    @RequestMapping(value = "/{dtid}", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult update(@PathVariable("dtid") long dtid,
                                @RequestBody SysDict sysDict){
        SysDict old_sysDict = sysDictRepository.findOne(dtid);

        if(null == old_sysDict){
            return RequestResult.fail("无法获取记录");
        }

        old_sysDict.setTypeCode(sysDict.getTypeCode());
        old_sysDict.setItemCode(sysDict.getItemCode());
        old_sysDict.setItemName(sysDict.getItemName());
        old_sysDict.setItemValue(sysDict.getItemValue());
        old_sysDict.setIsFixed(sysDict.getIsFixed());
        old_sysDict.setLastModifyBy(HeaderUtils.getCurrentUser());
        old_sysDict.setLastModifyTime(new Date());

        old_sysDict = sysDictRepository.saveAndFlush(old_sysDict);
        return RequestResult.success(old_sysDict);
    }

    @ApiOperation(value = "删除|SysDict")
    @DeleteMapping("")
    public RequestResult delete(@RequestHeader("Authorization") String Authorization,
                                @RequestParam(value = "id", required = true)  long dtid) {
        SysDict sysDict = sysDictRepository.findOne(dtid);

        if(null == sysDict){
            return RequestResult.fail("无法获取记录");
        }

        if(sysDict.getIsFixed() == CommonFlag.VALID.getValue()){
            return RequestResult.fail("系统默认，不可删除");
        }

        //sysDictRepository.delete(sysDict);
        sysDict.setFlag(CommonFlag.DELETED.getValue());
        sysDictRepository.saveAndFlush(sysDict);
        return  RequestResult.success("ok");
    }
}
