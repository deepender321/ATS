package com.leverage.ApplicationServices.controller;

import java.util.List;

import com.leverage.ApplicationServices.DTO.InterviewDetailsRequestDto;
import com.leverage.ApplicationServices.model.*;
import com.leverage.ApplicationServices.service.InterviewDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.DTO.StatusUpdateDto;
import com.leverage.ApplicationServices.service.CandidateService;
import com.leverage.ApplicationServices.service.JobApplicationsService;
import com.leverage.ApplicationServices.service.MarketingMemberService;

@RestController
@RequestMapping("/marketingMember")
public class MarketingMemberController {

	private  MarketingMemberService marketingMemberService;
	private  CandidateService candidateService;
	private  JobApplicationsService jobApplicationsService;
	private InterviewDetailsService interviewDetailsService;

	public MarketingMemberController(MarketingMemberService marketingMemberService,
			CandidateService candidateService,JobApplicationsService jobApplicationsService,
									 InterviewDetailsService interviewDetailsService) {
		super();
		this.marketingMemberService = marketingMemberService;
		this.candidateService = candidateService;
		this.jobApplicationsService =jobApplicationsService;
		this.interviewDetailsService = interviewDetailsService;
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Integer userId,
			@RequestBody CreateUserRequestDto updatedUserRequestDto) {
		return marketingMemberService.updateUserByMember(userId, updatedUserRequestDto);
	}

	@PostMapping("/{candidateId}/resumes")
	public ResponseEntity<?> uploadResume(@PathVariable int candidateId, @RequestParam("file") MultipartFile[] file) {
		return candidateService.uploadResume(candidateId, file);
	}

	@GetMapping("/{candidateId}/resumes/{resumeId}")
	public ResponseEntity<?> getResume(@PathVariable int resumeId) {
		return candidateService.getResume(resumeId);
	}

	@GetMapping("/{candidateId}/resumes")
	public ResponseEntity<?> getAllResumes(@PathVariable int candidateId) {
		List<Resume> resumes = candidateService.getResumesByCandidateId(candidateId);
		if (resumes.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(resumes);
	}

	@DeleteMapping("/resumes/{resumeId}")
	public ResponseEntity<?> deleteResume(@PathVariable int resumeId) {
		candidateService.deleteResume(resumeId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/details/{userId}")
	public ResponseEntity<?> getMemberDetailsByUserId(@PathVariable int userId) {
		return marketingMemberService.getMemberDetailsByUserId(userId);

	}

//	@GetMapping("/allUsers")
//	public List<User> getAllUsers() {
//	//	log.info("get All users admin");
//		return marketingMemberService.getAllUsers();
//	}
	@GetMapping("/candidate/{userId}")
	public Candidate getCandidateByUserId(@PathVariable int userId) {
		return candidateService.getCandidateByUserId(userId);
	}

	@GetMapping("/allUsers")
	public List<MarketingMember> getAllUsers() {
		//log.info("get All users admin");
		return marketingMemberService.getAllMarketingMembers();
	}

	@GetMapping("/allUses")
	public List<Candidate> getAlUsers() {
		//log.info("get All users admin");
		return candidateService.getAllCandidates();
	}

	@GetMapping("/{userId}")
	public MarketingMember getMemberByUserId(@PathVariable int userId) {
		return marketingMemberService.getMarketingMemberByUserId(userId);
	}

	@GetMapping("/{userId}/jobApplications")
	public ResponseEntity<?> getJobApplicationsByMarketingMemberId(@PathVariable int userId) {
		MarketingMember mm = marketingMemberService.getMarketingMemberByUserId(userId);
		List<JobApplications> jobApplications = jobApplicationsService.getJobApplicationsByMarketingMemberId(mm.getId());
		if (jobApplications.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(jobApplications);
	}
    @PostMapping("/{userId}/jobApplications")
    public ResponseEntity<?> createJobApplication(@PathVariable int userId, @RequestBody JobApplications jobApplications) {
        jobApplications.setMarketingMember(marketingMemberService.getMarketingMemberByUserId(userId));
        JobApplications savedJobApplication = jobApplicationsService.saveOrUpdateJobApplication(jobApplications);
        return ResponseEntity.ok(savedJobApplication);
    }
    
    @PutMapping("/{userId}/jobApplications")
	public ResponseEntity<?> updateJobApplication(@PathVariable int userId,
			@RequestBody JobApplications jobApplications) {
		JobApplications savedJobApplication = jobApplicationsService.updateJobApplication(jobApplications);
		return ResponseEntity.ok(savedJobApplication);
	}
    
    @PutMapping("/jobApplications/{jobId}/status")
    public ResponseEntity<?> updateJobApplicationStatus(@PathVariable int jobId, @RequestBody StatusUpdateDto statusUpdate) {
    
       return marketingMemberService.updateJobApplicationStatus(jobId, statusUpdate);
       
    }

	@PostMapping("/{jobId}/interviews")
	public ResponseEntity<?> addInterviewDetails(@PathVariable int jobId,
												@RequestBody InterviewDetailsRequestDto interviewDetailsRequestDto){
		return interviewDetailsService.addInterviewDetails(jobId,interviewDetailsRequestDto);
	}
}
