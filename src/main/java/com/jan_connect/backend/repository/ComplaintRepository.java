package com.jan_connect.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findBySubmittedByIdAndCityIdOrderByCreatedAtDesc(
            Long userId, Long cityId);

    List<Complaint> findByCityIdOrderByPriorityDescCreatedAtDesc(
            Long cityId);

    @Query("""
            SELECT MAX(CAST(SUBSTRING(c.complaaintNumber, 5) AS int))
            FROM Complaint c WHERE c.city.id = :cityId
            """)
    Optional<Integer> findMaxSequenceForCity(
            @Param("cityId") Long cityId);
}
