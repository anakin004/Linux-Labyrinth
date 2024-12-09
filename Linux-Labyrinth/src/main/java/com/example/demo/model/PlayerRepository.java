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
    int updatePathForUser(@Param("username") String username, @Param("value") String value);


    // since jpa does not support dyanmic col insertion, we must define an insertion for every answer column
    // we could set native flag to true, but I would rather avoid that
    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.answer_1 = :value WHERE p.username = :username")
    int updateAnswer1ForUser(@Param("username") String username, @Param("value") String value);

    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.answer_2 = :value WHERE p.username = :username")
    int updateAnswer2ForUser(@Param("username") String username, @Param("value") String value);

    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.answer_3 = :value WHERE p.username = :username")
    int updateAnswer3ForUser(@Param("username") String username, @Param("value") String value);

    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.answer_4 = :value WHERE p.username = :username")
    int updateAnswer4ForUser(@Param("username") String username, @Param("value") String value);

    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.answer_5 = :value WHERE p.username = :username")
    int updateAnswer5ForUser(@Param("username") String username, @Param("value") String value);

    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.answer_6 = :value WHERE p.username = :username")
    int updateAnswer6ForUser(@Param("username") String username, @Param("value") String value);

    @Modifying
    @Transactional
    @Query("UPDATE PlayerEntity p SET p.answer_7 = :value WHERE p.username = :username")
    int updateAnswer7ForUser(@Param("username") String username, @Param("value") String value);

}
