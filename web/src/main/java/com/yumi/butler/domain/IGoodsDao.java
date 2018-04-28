package com.yumi.butler.domain;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by zy on 2017/10/9.
 */
public interface IGoodsDao extends PagingAndSortingRepository<Goods, Long>, JpaSpecificationExecutor<Goods> {
}
