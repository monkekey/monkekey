package com.hpd.butler.domain;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ISOrderDao extends PagingAndSortingRepository<SOrder, Long>, JpaSpecificationExecutor<SOrder> {
}
