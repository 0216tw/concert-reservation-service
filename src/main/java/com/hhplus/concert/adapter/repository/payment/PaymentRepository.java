package com.hhplus.concert.adapter.repository.payment;

import com.hhplus.concert.domain.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
