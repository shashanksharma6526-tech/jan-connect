package com.jan_connect.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long>{
    List<City> findByStateAndIsActiveTrueOrderByNameAsc(Long stateID);
    Optional<City> findByIdAndIsActiveTrue(long id);
    boolean existByNameIgnoreCaseAndAtateId(String name, Long stateId);
}
