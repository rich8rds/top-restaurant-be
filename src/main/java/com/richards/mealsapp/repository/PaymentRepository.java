package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
