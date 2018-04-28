package com.yumi.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISCommodityRepository extends JpaRepository<SCommodity,Long> {

    public List<SCommodity> findByFlagOrderByCommodityCode(Integer flag);

    public Page<SCommodity> findByFlag(Pageable pageable, Integer flag);

    public Page<SCommodity> findAllByCommodityCategoryCodeInAndFlag(Pageable pageable, List<String> categoryCode,Integer flag);

    public SCommodity findByCommodityCodeAndFlag(String code,Integer flag);

}
