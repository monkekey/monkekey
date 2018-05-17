package com.hpd.butler.domain;

import com.hpd.butler.vo.InnVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zy on 2017/10/9.
 */
public interface IInnRepository  extends JpaRepository<Inn, Long> {

    public Inn findByInnCode(String innCode);

    public List<Inn> findByInnNameLike(String innName);

    @Query("select new com.hpd.butler.vo.InnVo(t.innCode, t.innName) from Inn t where t.flag = 1")
    public List<InnVo> findAllInn();

    //@Query(value = "SELECT i.* from inn i WHERE i.longitude is not null and i.latitude is not null and  EXISTS (SELECT 1 from sysuserdetail ud where ud.InnCode = i.InnCode)", nativeQuery=true)
    @Query("select new com.hpd.butler.vo.InnVo(t.innCode, t.innName, t.longitude, t.latitude) from Inn t where t.flag = 1 and t.longitude is not null and t.latitude is not null and  EXISTS (SELECT 1 from Sysuserdetail ud where ud.innCode = t.innCode) ")
    public List<InnVo> findValidInn();
}
