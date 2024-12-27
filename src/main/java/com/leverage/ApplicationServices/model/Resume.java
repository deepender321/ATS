package com.leverage.ApplicationServices.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Entity
@Table(name = "resume")
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="resume_id")
    private Integer id;
    private String fileName;
    private String filePath; 
	@Lob
    @Column(length = 100000 ,nullable = false)
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false,referencedColumnName = "candidate_id")
    private Candidate candidate;
	@CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
