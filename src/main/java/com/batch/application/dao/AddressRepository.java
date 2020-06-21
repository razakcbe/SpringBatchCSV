package com.batch.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.application.model.Address;

public interface AddressRepository extends JpaRepository<Address,Integer> {

}
