package com.jan_connect.backend.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.jan_connect.backend.dto.ComplaintRequest;
import com.jan_connect.backend.dto.ComplaintResponse;
import com.jan_connect.backend.dto.CursorPage;
import com.jan_connect.backend.entity.Community;
import com.jan_connect.backend.entity.Complaint;
import com.jan_connect.backend.entity.User;
import com.jan_connect.backend.exceptions.ResourceNotFoundExcepton;
import com.jan_connect.backend.repository.CommunityRepository;
import com.jan_connect.backend.repository.ComplaintRepository;
import com.jan_connect.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final RedisVoteService redisVoteService;

    // Submit a complaint
    public ComplaintResponse submitComplaint(ComplaintRequest request, String userEmail) {

        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundExcepton("User not found Exception"));

        Community community = communityRepository.findByCityAndIgnoreCaseAndIsActiveTrue(author.getCity())
                .orElseThrow(() -> new ResourceNotFoundExcepton(
                        "No active community found for your city: " + author.getCity()
                                + ". Please contact the administrator."));

        Complaint complaint = Complaint.builder()
        .title(request.getTitle())
        .description(request.getDescription())
        .category(request.getCategory())
        .location(request.getLocation())
        .author(author).community(community)
        .build();

        return mapToResponse(complaintRepository.save(complaint), author.getId());
    }

    // Cursor based frontpage
    public CursorPage<ComplaintResponse> getCityFrontPage(String city, String cursor, int limit, long requestingUserId){

        Community community = communityRepository.findByCityAndIgnoreCaseAndIsActiveTrue(city)
        .orElseThrow(() -> new ResourceNotFoundExcepton("No active community found for the city: "+city));

        Pageable pageable = PageRequest.of(0,limit+1);
        List<Complaint> complaints;

        if(cursor == null || cursor.isBlank())
            complaints = complaintRepository.findFirstPage(community.getId(),pageable);
        else{
            
        }
    }
}
