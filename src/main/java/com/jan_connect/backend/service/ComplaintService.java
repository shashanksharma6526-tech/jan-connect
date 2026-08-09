package com.jan_connect.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jan_connect.backend.dto.complaint.ComplaintRequest;
import com.jan_connect.backend.dto.complaint.ComplaintResponse;
import com.jan_connect.backend.dto.complaint.StatusUpdateRequest;
import com.jan_connect.backend.entity.City;
import com.jan_connect.backend.entity.Complaint;
import com.jan_connect.backend.entity.Department;
import com.jan_connect.backend.entity.User;
import com.jan_connect.backend.enums.ComplaintStatus;
import com.jan_connect.backend.exceptions.ResourceNotFoundException;
import com.jan_connect.backend.repository.CityRepository;
import com.jan_connect.backend.repository.ComplaintRepository;
import com.jan_connect.backend.repository.DepartmentRepository;
import com.jan_connect.backend.repository.UserRepository;
import com.jan_connect.backend.util.ComplaintIdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final ComplaintIdGenerator complaintIdGenerator;

    @Transactional
    public ComplaintResponse submitComplaint(ComplaintRequest request, String userEmail) {
        User author = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        City city = author.getCity();
        if (city == null) {
            throw new IllegalArgumentException("User must belong to a city to submit a complaint.");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));

        String complaintNumber = complaintIdGenerator.generateNextId(city);

        Complaint complaint = Complaint.builder()
                .complaintNumber(complaintNumber)
                .name(request.getName())
                .contact(request.getContact())
                .location(request.getLocation())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(ComplaintStatus.PENDING)
                .department(department)
                .submittedBy(author)
                .city(city)
                .build();

        return mapToResponse(complaintRepository.save(complaint));
    }

    public List<ComplaintResponse> getCityComplaints(Long cityId) {
        return complaintRepository.findByCityIdOrderByPriorityDescCreatedAtDesc(cityId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getUserComplaints(String userEmail, Long cityId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        return complaintRepository.findBySubmittedByIdAndCityIdOrderByCreatedAtDesc(user.getId(), cityId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ComplaintResponse getComplaintById(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID: " + complaintId));
        return mapToResponse(complaint);
    }

    @Transactional
    public ComplaintResponse updateStatus(Long complaintId, StatusUpdateRequest request) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with ID: " + complaintId));

        if (request.getStatus() != null) {
            complaint.setStatus(request.getStatus());
        }
        if (request.getAuthorityNote() != null) {
            complaint.setAuthorityNote(request.getAuthorityNote());
        }

        return mapToResponse(complaintRepository.save(complaint));
    }

    private ComplaintResponse mapToResponse(Complaint complaint) {
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
}
