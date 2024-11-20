package com.leverage.ApplicationServices.service;

import java.util.List;

import com.leverage.ApplicationServices.DTO.InterviewDetailsRequestDto;
import com.leverage.ApplicationServices.model.InterviewDetails;
import org.springframework.http.ResponseEntity;

public interface InterviewDetailsService {
	InterviewDetails getInterviewDetailsById(int interViewId);

	InterviewDetails saveOrUpdateInterviewDetails(InterviewDetails interviewDetails);

	void deleteInterviewDetails(int interViewId);

	List<InterviewDetails> getInterviewDetailsCandidateId(int candidateId);

	List<InterviewDetails> getInterviewDetailsByMarketingMemberId(int memberId);

	// void deleteInterviewDetailsByJobId(int candidateId);

	List<InterviewDetails> getInterviewDetailsByJobId(int jobId);

	InterviewDetails updateInterviewDetails(InterviewDetails interviewDetails);

	ResponseEntity<?> addInterviewDetails(int jobId, InterviewDetailsRequestDto interviewDetailsRequestDto);
}
