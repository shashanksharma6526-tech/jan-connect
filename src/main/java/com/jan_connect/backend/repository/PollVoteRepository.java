package com.jan_connect.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.PollVote;

@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, Long> {

    Optional<PollVote> findByVoterIdAandCityId(Long voterId, Long cityId);

    @Query("""
            SELECT p.optionIndex, COUNT(p)
            FROM POllVote p
            WHERE p.city.id = cityId
            GROUP BY p.optionIndex
            ORDER by p.optionIndex ASC
            """)
    List<Object[]> countVotesByOptionForCity(
            @Param("cityId") Long cityId);
}
