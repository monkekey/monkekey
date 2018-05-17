package com.hpd.butler.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zy on 2017/10/9.
 */
public interface IServicesRepository extends JpaRepository<Services, Long> {

    public Page<Services> findByFlagOrderBySortDesc(Pageable pageable, Integer flag);
}
