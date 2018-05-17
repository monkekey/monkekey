package com.hpd.butler.web;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.domain.SHotelCommodity;
import com.hpd.butler.service.HotelCommodityService;
import com.hpd.butler.utils.HeaderUtils;
import com.hpd.butler.vo.CommodityVO;
import com.hpd.butler.vo.HotelCommodityVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/HotelCommodity/dict")
@Api(value = "HotelCommodityController", description = "商品相关接口")
public class HotelCommodityController {

    @Autowired
    private HotelCommodityService hotelCommodityService;

    @ApiOperation(value = "查询|HotelCommodity")
    @GetMapping("queryHotelAndCommodityName")
    public RequestResult query(@RequestParam(value = "hotelID", required = true, defaultValue = "") final String hotelID,
                               @RequestParam(value = "keyword", required = false, defaultValue = "") final String keyword,
                               @RequestParam(value = "pageIdx", required = false, defaultValue = "0") Integer pageIdx,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize){
        List<CommodityVO> list = hotelCommodityService.queryHotelCommodity(pageIdx,pageSize,hotelID,keyword);

        return RequestResult.success(list);
    }

    @ApiOperation(value = "新增|HotelCommodity")
    @PostMapping("/save")
    public RequestResult add(@RequestBody HotelCommodityVO hotelCommodityVO){
        //Authorization:Bearer + " " + token

        String status = hotelCommodityService.save(hotelCommodityVO, HeaderUtils.getCurrentUser());

        return RequestResult.success(status);
    }

    @ApiOperation(value = "修改库存|HotelCommodity")
    @PutMapping("update")
    public RequestResult update(@RequestBody SHotelCommodity hotelCommodity){
        String status = hotelCommodityService.update(hotelCommodity,HeaderUtils.getCurrentUser());

        return RequestResult.success(status);

    }

    @ApiOperation(value = "删除门店商品|HotelCommodity")
    @DeleteMapping("del")
    public RequestResult del(@RequestParam(value = "id",required = false) Long id){
        String status = hotelCommodityService.del(id,HeaderUtils.getCurrentUser());

        if(status.equals("ok")){
            return RequestResult.success(status);
        }

        return RequestResult.fail(status);
    }
}
