package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Product;
import com.richards.mealsapp.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
}
