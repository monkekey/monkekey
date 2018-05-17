package com.hpd.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAdmireInfoRepository extends JpaRepository<AdmireInfo, Long> {

    @Query(value = "SELECT COUNT(c.id) FROM admireInfo c WHERE c.InnCode = :innCode AND c.name = :name", nativeQuery = true)
    public Object[] countComment(@Param("innCode") String innCode, @Param("name") String name);

}
