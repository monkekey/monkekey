package com.hpd.butler.web;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.constant.CommonFlag;
import com.hpd.butler.utils.HeaderUtils;
import com.hpd.butler.domain.IServicesRepository;
import com.hpd.butler.domain.Services;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by zy on 7/15/2017.
 */
@RestController
@RequestMapping("/sys/service")
@Api(value = "RoomController", description = "房间相关接口")
public class ServicesController {
    @Autowired
    private IServicesRepository iServicesRepository;

    @ApiOperation(value = "Services|列表")
    @GetMapping("")
    public RequestResult list(@RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        return RequestResult.success(iServicesRepository.findByFlagOrderBySortDesc(pageable, 1));
    }

    @ApiOperation(value = "Services|详情")
    @GetMapping("/{servicesId}")
    public RequestResult getByRoomCode(@NotNull @PathVariable("servicesId") long sid) {
        Services services = iServicesRepository.findOne(sid);

        if(null == services){
            return RequestResult.fail("无法获取记录");
        }
        return RequestResult.success(services);
    }

    @ApiOperation(value = "新增|Services")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestBody Services services){
        services.setCreateBy(HeaderUtils.getCurrentUser());
        services.setCreateTime(new Date());
        services.setLastModifyBy(HeaderUtils.getCurrentUser());
        services.setLastModifyTime(new Date());
        services.setFlag(CommonFlag.VALID.getValue());

        return RequestResult.success(iServicesRepository.saveAndFlush(services));
    }

    @ApiOperation(value = "更新|Services")
    @PutMapping("/{servicesId}")
    @Modifying
    @Transactional
    public RequestResult update(@NotNull @PathVariable("servicesId") long servicesId,
                                @RequestBody Services services){
        Services old_services = iServicesRepository.findOne(servicesId);

        if(null == old_services){
            return RequestResult.fail("无法获取记录");
        }

        try {
            PropertyUtils.copyProperties(old_services, services);
            old_services.setLastModifyTime(new Date());
            old_services.setLastModifyBy(HeaderUtils.getCurrentUser());
            iServicesRepository.saveAndFlush(old_services);

            return RequestResult.success(old_services);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return RequestResult.fail("error");
    }

    @ApiOperation(value = "删除|Services")
    @DeleteMapping("")
    public RequestResult delete(@RequestParam(value = "id", required = true)  long servicesId) {
        Services services = iServicesRepository.findOne(servicesId);

        if(null == services){
            return RequestResult.fail("无法获取记录");
        }

        services.setFlag(CommonFlag.DELETED.getValue());
        services.setLastModifyTime(new Date());
        services.setLastModifyBy(HeaderUtils.getCurrentUser());
        iServicesRepository.saveAndFlush(services);

        return  RequestResult.success("ok");
    }
}
