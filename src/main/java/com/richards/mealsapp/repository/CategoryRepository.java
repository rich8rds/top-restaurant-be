package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Category;
import com.richards.mealsapp.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
