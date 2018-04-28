package com.yumi.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by zy on 2017/10/9.
 */
public interface ISysuserRepository  extends JpaRepository<Sysuser, Long> {

    public Sysuser findByAccount(String account);

    public List<Sysuser> findByIdIn(List<Long> ids);


    @Query("select ss from Sysuserdetail sd, Sysuser ss where sd.uid = ss.id and sd.innCode = :innCode and ss.flag=1")
    Page<Sysuser> findByInnCode(@Param("innCode")String innCode, Pageable pageable);

    @Query("select ss from Sysuserdetail sd, Sysuser ss where sd.uid = ss.id and sd.innCode = :innCode and ss.flag=1 and (ss.mobile like concat('%',:keywords,'%') or ss.name like concat('%',:keywords,'%') or ss.account like concat('%',:keywords,'%'))")
    Page<Sysuser> findByInnCodeAndKeywords(@Param("innCode")String innCode, @Param("keywords")String keywords, Pageable pageable);
}
