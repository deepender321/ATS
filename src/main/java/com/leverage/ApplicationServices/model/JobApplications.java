package com.leverage.ApplicationServices.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.leverage.ApplicationServices.service.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "job_applications")
public class JobApplications {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_id")
	private Integer id;

	private String companyName;

	private String role;

	private String applicationPlatform;

	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id")
	private Candidate candidate;

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "member_id")
	private MarketingMember marketingMember;

	@OneToOne
	@JoinColumn(name = "resume_id", referencedColumnName = "resume_id")
	private Resume resume;

	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;

	private String technology;
	
	private String description;

}
