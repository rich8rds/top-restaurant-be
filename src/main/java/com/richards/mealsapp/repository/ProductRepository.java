package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Customer;
import com.richards.mealsapp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
