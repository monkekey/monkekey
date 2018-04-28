package com.yumi.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zy on 2018/2/6.
 */
public interface ISysDictRepository extends JpaRepository<SysDict, Long> {
    public List<SysDict> findByFlagOrderByTypeCode(Integer flag);

    public Page<SysDict> findByFlag(Pageable pageable, Integer flag);

//    @Query("select * from SysDict sd inner join SysDictType sdt on sd.typeCode=sdt.typeCode and sd.flag = :flag and sdt.flag=:flag")
//    public Page<SysDict> findByFlag(Pageable pageable, @Param("flag")Integer flag);
}
