package com.yumi.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zy on 2017/10/9.
 */
public interface ICustomReplyRepository extends JpaRepository<CustomReply, Long> {
    public List<CustomReply> findByUsercodeOrderByIsDefault(@Param("usercode") String usercode);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from CustomReply cr where cr.usercode = ?1 and cr.isDefault = 1")
    int updateDefaultByucode(String usercode);
}
