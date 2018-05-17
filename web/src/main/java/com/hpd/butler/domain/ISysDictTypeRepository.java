package com.hpd.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by zy on 2018/2/6.
 */
public interface ISysDictTypeRepository extends JpaRepository<SysDictType, Long> {
    public List<SysDictType> findByFlag(Integer flag);

    public Page<SysDictType> findByFlag(Pageable pageable, Integer flag);
}
