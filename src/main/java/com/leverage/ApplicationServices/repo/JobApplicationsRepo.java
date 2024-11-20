package com.leverage.ApplicationServices.repo;

import java.time.LocalDateTime;
import java.util.List;

import com.leverage.ApplicationServices.service.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import com.leverage.ApplicationServices.model.JobApplications;

public interface JobApplicationsRepo extends JpaRepository<JobApplications,Integer> {
	List<JobApplications> findByCandidate_Id(Integer candidateId);

    List<JobApplications> findByMarketingMember_Id(Integer memberId);
    void deleteByCandidate_Id(Integer Id);

	List<JobApplications> findBycreatedDateBetween(LocalDateTime startDate,
			LocalDateTime endDate);
    List<JobApplications> findByStatus(Status status);

    
  
}
