package com.hpd.butler.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISPaymentRepository extends JpaRepository<SPayment,Long> {
}
