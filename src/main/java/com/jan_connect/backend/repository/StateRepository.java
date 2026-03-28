package com.jan_connect.backend.repository;

import com.jan_connect.backend.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State,Long>{
    boolean existByNameIgnoreCase(String name);
}
