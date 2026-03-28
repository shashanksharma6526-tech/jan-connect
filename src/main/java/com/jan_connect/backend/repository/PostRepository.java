package com.jan_connect.backend.repository;

import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jan_connect.backend.entity.Post;
import com.jan_connect.backend.enums.PostCategory;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Innitial Loading
    @Query("""
            SELECT p FROM Post p
            WHERE p.city.id = :cityId
            ORDER BY p.voteScore DESC, p.id DESC
            """)
    List<Post> findFirstPage(
            @Param("cityId") Long cityId,
            Pageable pageable);

    // Cursor base subsequent loading
    @Query("""
            SELECT p FROM Post p
            WHERE p.city.id = :cityId
            AND (p.voteScore < :lastscore
            OR (p.voteScore = :lastScore AND p.id < :lastID))
            ORDER BY p.voteScore DESC, p.id DESC
            """)
            List<Post> findNextPage(
                @Param("cityId") Long cityId,
                @Param("lastScore") int lastScore,
                @Param("lastId") Long lastId,
                Pageable pageable
        );

    @Query("""
            SELECT p FROM Post p
            WHERE p.city.id = :cityId
            AND (:category IS NULL OR p.category = :category)
            ORDER BY (P.voteScore * 2 + (EXTRACT (EPOCH FROM p.createdAt) / 3600)) DESC,
            p.id DESC
            """)
            List<Post> findTrendingFirstPage(
                @Param("cityId") Long cityId,
                @Param("category") PostCategory category,
                Pageable pageable
            );

    long countByCityId(Long cityId);
}
