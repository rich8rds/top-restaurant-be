package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.PickupCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupCenterRepository extends JpaRepository<PickupCenter, Long> {
}
