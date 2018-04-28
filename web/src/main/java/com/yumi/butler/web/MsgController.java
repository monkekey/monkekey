package com.yumi.butler.web;

import com.yumi.butler.common.RequestResult;
import com.yumi.butler.service.MsgInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * Created by zy on 2018/1/12.
 */
@RestController
@RequestMapping("/sys/msg")
@Api(value = "MsgController", description = "消息相关接口")
public class MsgController {
    @Autowired
    private MsgInfoService msgInfoService;

    @ApiOperation(value = "更新已读|Msg")
    @PutMapping("")
    @Transactional
    public RequestResult add(@RequestParam(value = "openid", required = true) String openid,
                             @RequestParam(value = "checkinInn", required = true) String checkinInn,
                             @RequestParam(value = "fromGuest", required = false, defaultValue = "1") String fromGuest){
        msgInfoService.updateMsgStatus(checkinInn, openid, fromGuest);
        return RequestResult.success("ok");
    }
}
