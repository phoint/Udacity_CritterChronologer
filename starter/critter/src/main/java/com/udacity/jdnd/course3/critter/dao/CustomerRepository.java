package com.udacity.jdnd.course3.critter.dao;

import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c INNER JOIN c.pets p WHERE p.id = :petId")
    Customer findByPet(long petId);
}
