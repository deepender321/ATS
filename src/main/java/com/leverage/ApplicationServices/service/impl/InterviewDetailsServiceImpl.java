package com.leverage.ApplicationServices.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.leverage.ApplicationServices.DTO.InterviewDetailsRequestDto;
import com.leverage.ApplicationServices.model.*;
import com.leverage.ApplicationServices.repo.JobApplicationsRepo;
import com.leverage.ApplicationServices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.leverage.ApplicationServices.model.InterviewDetails;
import com.leverage.ApplicationServices.model.JobApplications;
import com.leverage.ApplicationServices.repo.InterviewDetailsRepo;
import com.leverage.ApplicationServices.service.InterviewDetailsService;
import com.leverage.ApplicationServices.service.JobApplicationsService;
import com.leverage.ApplicationServices.service.Status;

import jakarta.transaction.Transactional;
@Service
public class InterviewDetailsServiceImpl implements InterviewDetailsService {
	
	@Autowired
	private final InterviewDetailsRepo interviewDetailsRepo;
	private final JobApplicationsRepo jobApplicationsRepo;
	private JobApplicationsService jobApplicationsService;
	private final CandidateService candidateService;
	private final MarketingMemberService marketingMemberService;

	public InterviewDetailsServiceImpl(InterviewDetailsRepo interviewDetailsRepo, JobApplicationsRepo jobApplicationsRepo, JobApplicationsService jobApplicationsService,
	CandidateService candidateService, MarketingMemberService marketingMemberService) {
		this.interviewDetailsRepo = interviewDetailsRepo;
		this.jobApplicationsRepo = jobApplicationsRepo;
		this.jobApplicationsService = jobApplicationsService;
		this.candidateService = candidateService;
		this.marketingMemberService = marketingMemberService;
	}

	@Override
	public InterviewDetails getInterviewDetailsById(int interviewId) {
		
		return interviewDetailsRepo.findById(interviewId).orElse(null);
	}

	@Override
	@Transactional
	public InterviewDetails saveOrUpdateInterviewDetails(InterviewDetails interviewDetails) {
		JobApplications jobApplications = jobApplicationsService.getJobApplicationById(interviewDetails.getJobApplications().getId());
		if((jobApplications.getStatus().equals(Status.REJECTED))||(jobApplications.getStatus().equals(Status.SELECTED)) ) {
		return interviewDetailsRepo.save(interviewDetails);
        }
	    else {
		throw new IllegalArgumentException("could not update this table");
	    }
	}
	@Override
	public void deleteInterviewDetails(int interviewId) {
		interviewDetailsRepo.deleteById(interviewId);
		
	}

	@Override
	@Transactional
	public InterviewDetails updateInterviewDetails(InterviewDetails interviewDetails) {
		return interviewDetailsRepo.save(interviewDetails);
	}

	@Override
	public List<InterviewDetails> getInterviewDetailsByJobId(int jobId) {
		
		return interviewDetailsRepo.getInterviewDetailsByJobApplications_Id(jobId);
	}

	@Override
	public List<InterviewDetails> getInterviewDetailsCandidateId(int candidateId) {
		
		return interviewDetailsRepo.getInterviewDetailsByCandidate_Id(candidateId);
	}

	@Override
	public List<InterviewDetails> getInterviewDetailsByMarketingMemberId(int memberId) {
		return interviewDetailsRepo.getInterviewDetailsByMarketingMember_Id(memberId);
	}

	// Add interview details only to job applications with 'InProcess' status
	@Override
	public ResponseEntity<?> addInterviewDetails(int jobId, InterviewDetailsRequestDto interviewDetailsRequestDto) {
		// Fetch all job applications with 'InProcess' status
		List<JobApplications> inProcessApplications = jobApplicationsRepo.findByStatus(Status.INPROCESS);

		// Check if the job application with the provided jobId is in the 'InProcess' list
		JobApplications jobApplication = inProcessApplications.stream()
				.filter(app -> app.getId()== jobId)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No 'InProcess' JobApplication found with id " + jobId));


		// Create and populate InterviewDetails entity
		InterviewDetails interviewDetails = new InterviewDetails();
		interviewDetails.setJobApplications(jobApplication);
		interviewDetails.setCandidate(jobApplication.getCandidate());
		interviewDetails.setMarketingMember(jobApplication.getMarketingMember());
		interviewDetails.setResume(jobApplication.getResume());
		interviewDetails.setResult(interviewDetailsRequestDto.getResult());
		interviewDetails.setQuestions(interviewDetailsRequestDto.getQuestions());
		interviewDetails.setScheduledDate(interviewDetailsRequestDto.getScheduledDate());

		// Save the interview details
		InterviewDetails interviewDetailsSaved = interviewDetailsRepo.save(interviewDetails);

		return ResponseEntity.ok(interviewDetailsSaved);
	}


}
