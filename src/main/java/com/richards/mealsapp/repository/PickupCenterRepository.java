package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.PickupCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PickupCenterRepository extends JpaRepository<PickupCenter, Long> {
    Optional<PickupCenter> findByEmail(String pickupCenterEmail);
}
