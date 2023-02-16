package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
