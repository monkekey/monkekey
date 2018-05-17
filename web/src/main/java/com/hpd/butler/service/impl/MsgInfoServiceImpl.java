package com.hpd.butler.service.impl;

import com.hpd.butler.domain.IMsginfoRepository;
import com.hpd.butler.domain.Msginfo;
import com.hpd.butler.service.MsgInfoService;
import com.hpd.butler.vo.Guest;
import com.hpd.netty.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by teddyzhou on 2017/10/12.
 */
@Service
public class MsgInfoServiceImpl implements MsgInfoService {

    @Autowired
    private IMsginfoRepository iMsginfoRepository;

    public Msginfo saveMsgInfo(Msginfo msginfo){
        return iMsginfoRepository.saveAndFlush(msginfo);
    }

    public Msginfo updateMsgInfo(Msginfo msginfo){
        return iMsginfoRepository.saveAndFlush(msginfo);
    }

    public Page<Msginfo> findMsgList(String openid, String innCode, Integer pageIdx, Integer pageSize){
        Pageable pageable = new PageRequest(pageIdx, pageSize, Sort.Direction.DESC, "id");

        return iMsginfoRepository.findByOpenidAndCheckinInn(openid, innCode, pageable);
    }

    public List<Msginfo> findUnReadyMsg(String openid){
//        List<Msginfo> msgList =  iMsginfoRepository.findByOpenidAndFromGuestAndIsReadAndChatTimeGreaterThanEqualOrderByChatTime(openid, "0", 0, StringUtils.modifyDate(new Date(),0,0,-2,0,0,0));
        List<Msginfo> msgList =  iMsginfoRepository.findByOpenidAndChatTimeGreaterThanEqualOrderByChatTime(openid, StringUtils.modifyDate(new Date(),0,0,-3,0,0,0));
//        for (Msginfo msg : msgList){
//
//        }
        return msgList;
    }

    public List<Msginfo> findUnReadyGuestMsg(String checkinInn){
//        List<Msginfo> msgList =  iMsginfoRepository.findByCheckinInnAndFromGuestAndIsReadAndChatTimeGreaterThanEqualOrderByChatTime(checkinInn, "1", 0, StringUtils.modifyDate(new Date(),0,0,-2,0,0,0));
        List<Msginfo> msgList =  iMsginfoRepository.findByCheckinInnAndChatTimeGreaterThanEqualOrderByChatTime(checkinInn, StringUtils.modifyDate(new Date(),0,0,-3,0,0,0));
        return msgList;
    }

    public List<Guest> findUnReadMsgGuest(String checkinInn){
        List<Guest> guests = iMsginfoRepository.findUnReadMsgGuest(checkinInn, StringUtils.modifyDate(new Date(),0,0,-3,0,0,0));

        return guests;
    }

    public void updateMsgStatus(String checkinInn, String openid, String fromGuest){
        iMsginfoRepository.updateMsgStatus(checkinInn, openid, fromGuest);
    }
}
