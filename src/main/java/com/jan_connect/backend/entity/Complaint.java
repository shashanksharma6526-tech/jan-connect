package com.jan_connect.backend.entity;

import com.jan_connect.backend.enums.ComplaintPriority;
import com.jan_connect.backend.enums.ComplaintStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Formatted display ID owned by the backend (e.g. "CMP-001", "CMP-042")
    // Generated per city using the city's complaintSequence counter
    @Column(unique = true, nullable = false)
    private String complaintNumber;

    @Column(nullable = false)
    private String name;         // Name of the person filing the complaint

    @Column(nullable = false)
    private String contact;      // Phone or email for follow-up

    @Column(nullable = false)
    private String location;     // Where the problem is located

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ComplaintPriority priority = ComplaintPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ComplaintStatus status = ComplaintStatus.PENDING;

    // Authority note — set when status is updated
    private String authorityNote;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Which department this complaint is routed to
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User submittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}