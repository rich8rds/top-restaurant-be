package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Order;
import com.richards.mealsapp.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}
