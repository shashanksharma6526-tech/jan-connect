package com.jan_connect.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.Community;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>{
    
    Optional<Community> findByCityAndIgnoreCaseAndIsActiveTrue(String city);

    List<Community> findAllOrderByCityAsc();

    List<Community> findByIsActiveTrueOrderByCityAsc();

    boolean existsByCityIgnoreCase(String city);
}
