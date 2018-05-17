package com.hpd.butler.service;

import com.hpd.butler.domain.Msginfo;
import com.hpd.butler.vo.Guest;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by teddyzhou on 2017/10/12.
 */
public interface MsgInfoService {
    public Msginfo saveMsgInfo(Msginfo msginfo);

    public Msginfo updateMsgInfo(Msginfo msginfo);

    public Page<Msginfo> findMsgList(String openid, String innCode, Integer pageIdx, Integer pageSize);

    public List<Msginfo> findUnReadyMsg(String openid);

    public List<Msginfo> findUnReadyGuestMsg(String openid);

    public List<Guest> findUnReadMsgGuest(String checkinInn);

    public void updateMsgStatus(String checkinInn, String openid, String fromGuest);
}
