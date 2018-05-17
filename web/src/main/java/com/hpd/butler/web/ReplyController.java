package com.hpd.butler.web;

import com.hpd.butler.common.RequestResult;
import com.hpd.butler.domain.CustomReply;
import com.hpd.butler.domain.ICustomReplyRepository;
import com.hpd.butler.utils.HeaderUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zy on 2017/12/20.
 */
@RestController
@RequestMapping("/sys/replay")
@Api(value = "ReplyController", description = "自定义回复相关接口")
public class ReplyController {
    @Autowired
    private ICustomReplyRepository customReplyRepository;

    @ApiOperation(value = "All CustomReply|列表")
    @GetMapping("")
    public RequestResult getAllReplyByUsercode(@RequestHeader("Authorization") String Authorization){
        return RequestResult.success(customReplyRepository.findByUsercodeOrderByIsDefault(HeaderUtils.getCurrentUser()));
    }

    @ApiOperation(value = "新增|CustomReply")
    @PostMapping("")
    @Transactional
    public RequestResult add(@RequestHeader("Authorization") String Authorization,
                             @RequestBody CustomReply reply){
        //Authorization:Bearer + " " + token
        reply.setUsercode(HeaderUtils.getCurrentUser());
        if (reply.getIsDefault() == 1){
            customReplyRepository.updateDefaultByucode(HeaderUtils.getCurrentUser());
        }

        reply = customReplyRepository.saveAndFlush(reply);
        return RequestResult.success(reply);
    }

    @ApiOperation(value = "更新|CustomReply")
    @RequestMapping(value = "/{replyId}", method = RequestMethod.PUT)
    @Modifying
    @Transactional
    public RequestResult update(@RequestHeader("Authorization") String Authorization,
                                @PathVariable("replyId") long replyId,
                                @RequestBody CustomReply reply){
        CustomReply old_reply = customReplyRepository.findOne(replyId);

        if(null == old_reply){
            return RequestResult.fail("无法获取记录");
        }

        if (reply.getIsDefault() == 1){
            customReplyRepository.updateDefaultByucode(HeaderUtils.getCurrentUser());
        }

        old_reply.setContent(reply.getContent());
        old_reply.setIsDefault(reply.getIsDefault());
        old_reply = customReplyRepository.saveAndFlush(old_reply);
        return RequestResult.success(old_reply);
    }

    @ApiOperation(value = "删除|CustomReply")
    @DeleteMapping("")
    public RequestResult delete(@RequestHeader("Authorization") String Authorization,
                                @RequestParam(value = "id", required = true)  long replyId) {
        CustomReply reply = customReplyRepository.findOne(replyId);

        if(null == reply){
            return RequestResult.fail("无法获取记录");
        }

        customReplyRepository.delete(reply);
        return  RequestResult.success("ok");
    }
}
