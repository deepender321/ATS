package com.leverage.ApplicationServices.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leverage.ApplicationServices.model.InterviewDetails;

public interface InterviewDetailsRepo extends JpaRepository<InterviewDetails, Integer> {

	List<InterviewDetails> getInterviewDetailsByJobApplications_Id(int jobId);

	List<InterviewDetails> getInterviewDetailsByCandidate_Id(int candidateId);

	List<InterviewDetails> getInterviewDetailsByMarketingMember_Id(int memberId);

}
