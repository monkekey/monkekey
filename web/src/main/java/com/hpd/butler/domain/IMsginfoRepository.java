package com.hpd.butler.domain;

import com.hpd.butler.vo.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


/**
 * Created by teddyzhou on 2017/10/12.
 */
public interface IMsginfoRepository extends JpaRepository<Msginfo, Long> {
    public Page<Msginfo> findByOpenidAndCheckinInn(String openId, String checkinInn, Pageable pageable);

//    public List<Msginfo> findByOpenidAndFromGuestAndIsReadAndChatTimeGreaterThanEqualOrderByChatTime(String openid, String fromGuest, Integer isread, Date queryDt);

//    public List<Msginfo> findByCheckinInnAndFromGuestAndIsReadAndChatTimeGreaterThanEqualOrderByChatTime(String checkinInn, String fromGuest, Integer isread, Date queryDt);

//@Query(value = "select new com.lavande.butler.vo.Guest(v.openid, v.guestName) from Msginfo v where v.checkinInn = :checkinInn and v.fromGuest = :fromGuest and v.isRead = 0 and v.chatTime>=:chatTime group by openid, guestName")
//public List<Guest> findUnReadMsgGuest(@Param("checkinInn") String checkinInn, @Param("fromGuest") String fromGuest, @Param("chatTime") Date chatTime);

    public List<Msginfo> findByOpenidAndChatTimeGreaterThanEqualOrderByChatTime(String openid, Date queryDt);

    public List<Msginfo> findByCheckinInnAndChatTimeGreaterThanEqualOrderByChatTime(String openid, Date queryDt);

    @Query(value = "select new com.hpd.butler.vo.Guest(v.openid, v.guestName) from Msginfo v where v.checkinInn = :checkinInn  and v.fromGuest = '1' and v.chatTime>=:chatTime group by openid, guestName")
    public List<Guest> findUnReadMsgGuest(@Param("checkinInn") String checkinInn, @Param("chatTime") Date chatTime);

    @Modifying
    @Query("update Msginfo set isRead = 1 where checkinInn = :checkinInn  and openid = :openid and fromGuest = :fromGuest and isRead = 0")
    public void updateMsgStatus(@Param("checkinInn") String checkinInn, @Param("openid") String openid, @Param("fromGuest") String fromGuest);
}
