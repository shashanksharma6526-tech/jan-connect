package com.jan_connect.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findByComplaintIdAndParentCommentIsNullOrderByCreatedAtDesc(Long complaintId);
}