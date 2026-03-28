package com.jan_connect.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.Vote;
import com.jan_connect.backend.enums.VoteDirection;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long>{
    Optional<Vote> findByVoterIdAndPostId(Long voterId, Long postId);
    long countByPostIdAndDirection(Long postId, VoteDirection direction);
}
