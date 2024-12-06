package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    // JpaRepository provides basic CRUD methods (save, findById, findAll, etc.)
    // Jpa will also provide methods based on method names, so we don't need to provide implementation.

    PlayerEntity findByUsername(String username);
}
