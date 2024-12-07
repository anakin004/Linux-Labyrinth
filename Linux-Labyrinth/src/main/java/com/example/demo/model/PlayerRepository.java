package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;


public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    // JpaRepository provides basic CRUD methods (save, findById, findAll, etc.)
    // Jpa will also provide methods based on method names, so we don't need to provide implementation.

    PlayerEntity findByUsername(String username);

    /*
    by default JPQL defaults methods to select, so we need to say we are modifying
    we also need to define transactional since that is not included by default - since we arent reading
    we can set a query to write custom jpql
    */
    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.currentpath = :value WHERE p.username = :username")
    int updateColumnForUser(@Param("username") String username, @Param("value") String value);
}
