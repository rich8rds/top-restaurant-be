package com.richards.mealsapp.repository;

import com.richards.mealsapp.entity.Address;
import com.richards.mealsapp.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
