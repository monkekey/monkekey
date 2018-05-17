package com.hpd.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISCategoryRepository extends JpaRepository<SCategory, Long> {

    public List<SCategory> findByFlag(Integer flag);

    public Page<SCategory> findByFlag(Pageable pageable, Integer flag);

    public Page<SCategory> findByCategoryNameContainingAndFlag(String keywords,Pageable pageable, Integer flag);



}


