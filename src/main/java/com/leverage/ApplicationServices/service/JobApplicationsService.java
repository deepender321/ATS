package com.leverage.ApplicationServices.service;

import com.leverage.ApplicationServices.model.JobApplications;
import java.util.List;

public interface JobApplicationsService {

    JobApplications getJobApplicationById(int jobId);
    
    JobApplications saveOrUpdateJobApplication(JobApplications jobApplications);
    
    void deleteJobApplication(int jobId);
    
    List<JobApplications> getJobApplicationsByCandidateId(int candidateId);
    
    List<JobApplications> getJobApplicationsByMarketingMemberId(int memberId);
    
    // New method to update job application status
    JobApplications updateJobApplicationStatus(int jobId, Status newStatus);

	JobApplications updateJobApplication(JobApplications jobApplication);

	void deleteJobApplicationByCandidateId(int candidateId);

    JobApplications getJobApplicationsByStatus();

	List<JobApplications> getAllJobDetails();
	

	//List<JobApplications> getJobApplicationsByUserId(int userId);
}
