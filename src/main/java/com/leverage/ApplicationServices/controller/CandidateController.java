package com.leverage.ApplicationServices.controller;

import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.model.Candidate;
import com.leverage.ApplicationServices.model.JobApplications;
import com.leverage.ApplicationServices.model.MarketingMember;
import com.leverage.ApplicationServices.service.CandidateService;
import com.leverage.ApplicationServices.service.InterviewDetailsService;
import com.leverage.ApplicationServices.service.JobApplicationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import com.leverage.ApplicationServices.model.Resume;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

	private CandidateService candidateService;
	private JobApplicationsService jobApplicationsService;

	public CandidateController(CandidateService candidateService, JobApplicationsService jobApplicationService,
			InterviewDetailsService interviewDetailsService) {
		super();
		this.candidateService = candidateService;
		this.jobApplicationsService = jobApplicationService;
	}
	
	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Integer userId,
			@RequestBody CreateUserRequestDto createUserRequestDto) {
		return candidateService.updateUserByCandidate(userId, createUserRequestDto);
	}

	@PostMapping("/{userId}/resumes")
	public ResponseEntity<?> uploadResume(@PathVariable int userId, @RequestParam("files") MultipartFile[] files) {
		return candidateService.uploadResume(userId, files);
	}

	@GetMapping("/{candidateId}/resumes/{resumeId}")
	public ResponseEntity<Resume> getResume(@PathVariable int resumeId) {
		return (ResponseEntity<Resume>) candidateService.getResume(resumeId);
	}

	@GetMapping("/{candidateId}/resumes")
	public ResponseEntity<byte[]> getAllResumesByZip(@PathVariable int candidateId) {
		return candidateService.getAllResumesByCandidateId(candidateId);
	}

	@GetMapping("/{candidateId}/resumeLinks")
	public ResponseEntity<?> getAllResumesByLinks(@PathVariable int candidateId) {

		return candidateService.getAllResumesByCandidateIdBylinks(candidateId);
	}

	@DeleteMapping("/resumes/{resumeId}")
	public ResponseEntity<?> deleteResume(@PathVariable int resumeId) {
		candidateService.deleteResume(resumeId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/details/{userId}")
	public ResponseEntity<?> getCandidateById(@PathVariable int userId) {
		return candidateService.getCandidateDetailsByUserId(userId);
	}
	
	@GetMapping("/{userId}")
	public Candidate getMemberByUserId(@PathVariable int userId) {
		return candidateService.getCandidateByUserId(userId);
	}

	@PostMapping("/{userId}/jobApplications")
	public ResponseEntity<?> createJobApplication(@PathVariable int userId,
			@RequestBody JobApplications jobApplications) {
		jobApplications.setCandidate(candidateService.getCandidateByUserId(userId));
		JobApplications savedJobApplication = jobApplicationsService.saveOrUpdateJobApplication(jobApplications);
		return ResponseEntity.ok(savedJobApplication);
	}
	@PutMapping("/{userId}/jobApplications")
	public ResponseEntity<?> updateJobApplication(@PathVariable int userId,
			@RequestBody JobApplications jobApplications) {
		JobApplications savedJobApplication = jobApplicationsService.updateJobApplication(jobApplications);
		return ResponseEntity.ok(savedJobApplication);
	}

	@GetMapping("/{userId}/jobApplications")
	public ResponseEntity<?> getJobApplicationsByCandidateId(@PathVariable int userId) {
		Candidate candidate = candidateService.getCandidateByUserId(userId);
		List<JobApplications> jobApplications = jobApplicationsService
				.getJobApplicationsByCandidateId(candidate.getId());
		if (jobApplications.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(jobApplications);
	}
}
