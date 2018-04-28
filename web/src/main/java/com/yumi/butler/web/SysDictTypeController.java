package com.yumi.butler.web;

import com.yumi.butler.common.RequestResult;
import com.yumi.butler.constant.CommonFlag;
import com.yumi.butler.domain.ISCategoryRepository;
import com.yumi.butler.domain.ISysDictTypeRepository;
import com.yumi.butler.domain.SCategory;
import com.yumi.butler.domain.SysDictType;
import com.yumi.butler.utils.HeaderUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by zy on 2018/2/6.
 */
@RestController
@RequestMapping("/sys/dicttype")
@Api(value = "SysDictTypeController", description = "字典类型相关接口")
public class SysDictTypeController {
    @Autowired
    private ISCategoryRepository isCategoryRepository;

    @ApiOperation(value = "SysDictType|列表")
    @GetMapping("")
    public RequestResult getAll(@RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "keywords") String keywords){

        PageRequest pageRequest= new PageRequest(pageIdx, pageSize, Sort.Direction.ASC, "id");
        Page<SCategory> sCategoryPage = null;
        if(keywords != null && !keywords.equals("")){
            sCategoryPage = isCategoryRepository.findByCategoryNameContainingAndFlag(keywords,pageRequest,CommonFlag.VALID.getValue());
        }else{
            sCategoryPage = isCategoryRepository.findByFlag(pageRequest, CommonFlag.VALID.getValue());
        }

        return RequestResult.success(sCategoryPage);
    }

    @ApiOperation(value = "All SysDictType|列表")
    @GetMapping("/all")
    public RequestResult getAllDictType(){
        return RequestResult.success(isCategoryRepository.findByFlag(CommonFlag.VALID.getValue()));
    }

    @ApiOperation(value = "SysDictType|详情")
    @GetMapping("/{dtid}")
    public RequestResult getDictType(@NotNull @PathVariable("dtid") long dtid) {
        SCategory sCategory = isCategoryRepository.findOne(dtid);

        if(null == sCategory){
            return RequestResult.fail("无法获取记录");
        }
        return RequestResult.success(sCategory);
    }

    @ApiOperation(value = "新增|SysDictType")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody SCategory sCategory){
        //Authorization:Bearer + " " + token
        sCategory.setCategoryCode("CG"+new Date().getTime());
        sCategory.setCategoryCrateby(HeaderUtils.getCurrentUser());
        sCategory.setCategoryCratetime(new Date());
        sCategory.setCategoryLastby(HeaderUtils.getCurrentUser());
        sCategory.setCategoryLasttime(new Date());
        sCategory.setFlag(CommonFlag.VALID.getValue());

        sCategory = isCategoryRepository.saveAndFlush(sCategory);
        return RequestResult.success(sCategory);
    }

    @ApiOperation(value = "更新|SysDictType")
    @RequestMapping(value = "/{sdtid}", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult update(@PathVariable("sdtid") long sdtid,
                                @RequestBody SCategory sysDictType){
        SCategory old_sCategory = isCategoryRepository.findOne(sdtid);

        if(null == old_sCategory){
            return RequestResult.fail("无法获取记录");
        }

        old_sCategory.setCategoryName(sysDictType.getCategoryName());
        old_sCategory.setCategoryIsFixed(sysDictType.getCategoryIsFixed());
        old_sCategory.setCategoryLastby(HeaderUtils.getCurrentUser());
        old_sCategory.setCategoryLasttime(new Date());

        old_sCategory = isCategoryRepository.saveAndFlush(old_sCategory);
        return RequestResult.success(old_sCategory);
    }

    @ApiOperation(value = "删除|SysDictType")
    @DeleteMapping("")
    public RequestResult delete(@RequestHeader("Authorization") String Authorization,
                                @RequestParam(value = "id", required = true)  long sdtid) {
        SCategory sCategory = isCategoryRepository.findOne(sdtid);

        if(null == sCategory){
            return RequestResult.fail("无法获取记录");
        }

        if(sCategory.getCategoryIsFixed() == CommonFlag.VALID.getValue()){
            return RequestResult.fail("系统默认，不可删除");
        }

        //sysDictTypeRepository.delete(sysDictType);
        sCategory.setFlag(CommonFlag.DELETED.getValue());
        isCategoryRepository.saveAndFlush(sCategory);
        return  RequestResult.success("ok");
    }
}
