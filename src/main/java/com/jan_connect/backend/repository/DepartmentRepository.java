package com.jan_connect.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{
    List<Department> findByCityIdAndIsActiveTrueOrderByLabelAsc(Long cityId);
}
