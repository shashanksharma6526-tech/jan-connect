package com.jan_connect.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jan_connect.backend.dto.comment.CommentResponse;
import com.jan_connect.backend.dto.common.CursorPage;
import com.jan_connect.backend.dto.post.PostRequest;
import com.jan_connect.backend.dto.post.PostResponse;
import com.jan_connect.backend.entity.City;
import com.jan_connect.backend.entity.Post;
import com.jan_connect.backend.entity.User;
import com.jan_connect.backend.exceptions.ResourceNotFoundException;
import com.jan_connect.backend.exceptions.UnauthorizedException;
import com.jan_connect.backend.repository.CityRepository;
import com.jan_connect.backend.repository.PostRepository;
import com.jan_connect.backend.repository.UserRepository;
import com.jan_connect.backend.util.CursorUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    public CursorPage<PostResponse> getCityFeed(Long cityId, String cursor, int limit, Long requestingUserId) {
        Pageable pageable = PageRequest.of(0, limit + 1);
        List<Post> posts;

        CursorUtil.CursorData cursorData = CursorUtil.decode(cursor);
        if (cursorData == null) {
            posts = postRepository.findFirstPage(cityId, pageable);
        } else {
            posts = postRepository.findNextPage(cityId, cursorData.score(), cursorData.id(), pageable);
        }

        boolean hasMore = posts.size() > limit;
        if (hasMore) {
            posts = posts.subList(0, limit);
        }

        String nextCursor = null;
        if (!posts.isEmpty() && hasMore) {
            Post last = posts.get(posts.size() - 1);
            nextCursor = CursorUtil.encode(last.getVoteScore() != null ? last.getVoteScore() : 0, last.getId());
        }

        List<PostResponse> items = posts.stream()
                .map(p -> mapToResponse(p, requestingUserId))
                .collect(Collectors.toList());

        return new CursorPage<>(items, nextCursor, hasMore, items.size());
    }

    @Transactional
    public PostResponse createPost(Long cityId, PostRequest request, String userEmail) {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        City city = cityRepository.findByIdAndIsActiveTrue(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + cityId));

        Post post = Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .emoji(request.getEmoji())
                .author(author)
                .city(city)
                .voteScore(0)
                .build();

        return mapToResponse(postRepository.save(post), author.getId());
    }

    public PostResponse getPostById(Long postId, Long requestingUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        return mapToResponse(post, requestingUserId);
    }

    @Transactional
    public void deletePost(Long postId, String userEmail) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        if (!post.getAuthor().getId().equals(user.getId()) &&
                !user.getType().name().contains("ADMIN")) {
            throw new UnauthorizedException("You are not authorized to delete this post.");
        }

        postRepository.delete(post);
    }

    private PostResponse mapToResponse(Post post, Long requestingUserId) {
        List<CommentResponse> comments = post.getComments() != null
                ? post.getComments().stream().map(c -> CommentResponse.builder()
                        .id(c.getId())
                        .content(c.getContent())
                        .authorId(c.getAuthor() != null ? c.getAuthor().getId() : null)
                        .authorUsername(c.getAuthor() != null ? c.getAuthor().getUsername() : null)
                        .authorAvatar(c.getAuthor() != null ? c.getAuthor().getAvatar() : null)
                        .createdAt(c.getCreatedAt())
                        .build()).collect(Collectors.toList())
                : List.of();

        return PostResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .description(post.getDescription())
                .imageUrl(post.getImageUrl())
                .emoji(post.getEmoji())
                .voteScore(post.getVoteScore() != null ? post.getVoteScore() : 0)
                .authorId(post.getAuthor() != null ? post.getAuthor().getId() : null)
                .authorUsername(post.getAuthor() != null ? post.getAuthor().getUsername() : null)
                .authorAvatar(post.getAuthor() != null ? post.getAuthor().getAvatar() : null)
                .cityId(post.getCity() != null ? post.getCity().getId() : null)
                .cityName(post.getCity() != null ? post.getCity().getName() : null)
                .createdAt(post.getCreatedAt())
                .commentCount(comments.size())
                .comments(comments)
                .build();
    }
}
