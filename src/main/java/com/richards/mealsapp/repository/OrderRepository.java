package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Cart;
import com.richards.mealsapp.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
