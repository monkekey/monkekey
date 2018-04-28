package com.yumi.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT COUNT(c.id),SUM(c.starLavel) FROM comment c WHERE c.InnCode = :innCode AND c.customerServices = :customerServices", nativeQuery = true)
    public Object[] countComment(@Param("innCode") String innCode, @Param("customerServices") String customerServices);

    @Query(value = "SELECT c.starLavel,COUNT(c.id),SUM(c.starLavel) FROM comment c WHERE c.InnCode = :innCode AND c.customerServices = :customerServices GROUP BY c.starLavel", nativeQuery = true)
    public Object[] countStarLavel(@Param("innCode") String innCode, @Param("customerServices") String customerServices);

}
