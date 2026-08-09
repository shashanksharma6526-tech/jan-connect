package com.jan_connect.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jan_connect.backend.dto.complaint.ComplaintResponse;
import com.jan_connect.backend.dto.user.UserProfileResponse;
import com.jan_connect.backend.entity.Complaint;
import com.jan_connect.backend.entity.Post;
import com.jan_connect.backend.entity.User;
import com.jan_connect.backend.enums.UserType;
import com.jan_connect.backend.exceptions.ResourceNotFoundException;
import com.jan_connect.backend.repository.ComplaintRepository;
import com.jan_connect.backend.repository.PostRepository;
import com.jan_connect.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ComplaintRepository complaintRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll().stream().map(this::mapComplaintToResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
        postRepository.delete(post);
    }

    public List<UserProfileResponse> listUsers(String type) {
        List<User> users;
        if (type != null && !type.isBlank()) {
            try {
                UserType userType = UserType.valueOf(type.toUpperCase());
                users = userRepository.findAll().stream()
                        .filter(u -> u.getType() == userType)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                users = userRepository.findAll();
            }
        } else {
            users = userRepository.findAll();
        }

        return users.stream().map(this::mapUserToResponse).collect(Collectors.toList());
    }

    private ComplaintResponse mapComplaintToResponse(Complaint complaint) {
        return ComplaintResponse.builder()
                .id(complaint.getId())
                .complaintNumber(complaint.getComplaintNumber())
                .name(complaint.getName())
                .contact(complaint.getContact())
                .description(complaint.getDescription())
                .location(complaint.getLocation())
                .priority(complaint.getPriority())
                .status(complaint.getStatus())
                .authorityNote(complaint.getAuthorityNote())
                .departmentId(complaint.getDepartment() != null ? complaint.getDepartment().getId() : null)
                .departmentLabel(complaint.getDepartment() != null ? complaint.getDepartment().getLabel() : null)
                .departmentIcon(complaint.getDepartment() != null ? complaint.getDepartment().getIcon() : null)
                .cityId(complaint.getCity() != null ? complaint.getCity().getId() : null)
                .cityName(complaint.getCity() != null ? complaint.getCity().getName() : null)
                .submittedById(complaint.getSubmittedBy() != null ? complaint.getSubmittedBy().getId() : null)
                .submittedByUsername(complaint.getSubmittedBy() != null ? complaint.getSubmittedBy().getUsername() : null)
                .createdAt(complaint.getCreatedAt())
                .build();
    }

    private UserProfileResponse mapUserToResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .type(user.getType())
                .karma(user.getKarma())
                .cityId(user.getCity() != null ? user.getCity().getId() : null)
                .cityName(user.getCity() != null ? user.getCity().getName() : null)
                .build();
    }
}
