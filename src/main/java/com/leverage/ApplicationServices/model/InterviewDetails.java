package com.leverage.ApplicationServices.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "interview_details")
public class InterviewDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "interview_id")
	private Integer id;
	private String result;
	private String questions;
	private LocalDateTime scheduledDate;
	@ManyToOne
	@JoinColumn(name = "candidate_id", referencedColumnName = "candidate_id")
	private Candidate candidate;
	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "member_id")
	private MarketingMember marketingMember;
	@ManyToOne
	@JoinColumn(name = "resume_id", referencedColumnName = "resume_id")
	private Resume resume;
	@OneToOne
	@JoinColumn(name = "job_id", referencedColumnName = "job_id")
	private JobApplications jobApplications;
	@CreationTimestamp
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;
	@UpdateTimestamp
	@Column(name = "updated_date")
	private LocalDateTime updatedDate;
}
