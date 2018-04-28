package com.yumi.butler.domain;

import com.yumi.butler.vo.UserVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by teddyzhou on 2017/10/13.
 */
public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

    public Favorite findByInnCodeAndAccountAndOpenid(String inncode, String account, String openid);

    public int countByInnCodeAndAccountAndFlag(String inncode, String account, Integer flag);

    @Query(value = "select new com.yumi.butler.vo.UserVo(v.innCode, v.account,count(1)) from Favorite v where v.innCode = :inncode and v.flag = 1 group by innCode, account")
    public List<UserVo> findFavoriteCount(@Param("inncode") String inncode);

    @Query(value = "select new com.yumi.butler.vo.UserVo(v.innCode, v.account,count(*)) from Favorite v where v.innCode = :inncode and v.openid = :openid and v.flag = 1 group by innCode, account")
    public List<UserVo> findFavoriteCountByOpenid(@Param("inncode") String inncode, @Param("openid") String openid);
}
