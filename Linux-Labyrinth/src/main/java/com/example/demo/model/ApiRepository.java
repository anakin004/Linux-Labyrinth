package com.example.demo.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;                             
import org.springframework.data.repository.query.Param;  


public interface ApiRepository extends JpaRepository<ApiEntity, Long> {

	boolean existsByApiKey(String apiKey);
	ApiEntity findByApiKey(String apiKey);	
	
	@Modifying
        @Transactional
        @Query("UPDATE ApiEntity a SET a.remaining_uses = a.remaining_uses - 1 WHERE a.apiKey = :apiKey AND a.remaining_uses > 0")
        int reduceRemainingCountByApiKey(@Param("apiKey") String apiKey);
}
