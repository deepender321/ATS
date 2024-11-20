package com.leverage.ApplicationServices.service.impl;

import com.leverage.ApplicationServices.model.JobApplications;
import com.leverage.ApplicationServices.repo.JobApplicationsRepo;
import com.leverage.ApplicationServices.service.JobApplicationsService;
import com.leverage.ApplicationServices.service.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationsServiceImpl implements JobApplicationsService {

    @Autowired
    private JobApplicationsRepo jobApplicationsRepo;

    @Override
    public JobApplications getJobApplicationById(int jobId) {
        return jobApplicationsRepo.findById(jobId).orElse(null);
    }

    @Override
    public JobApplications saveOrUpdateJobApplication(JobApplications jobApplications) {
        return jobApplicationsRepo.save(jobApplications);
    }

    @Override
    public void deleteJobApplication(int jobId) {
        jobApplicationsRepo.deleteById(jobId);
    }
    @Override
    public void deleteJobApplicationByCandidateId(int candidateId) {
        jobApplicationsRepo.deleteByCandidate_Id(candidateId);
    }

    @Override
    public JobApplications getJobApplicationsByStatus() {
        return null;
    }

    @Override
    public List<JobApplications> getJobApplicationsByCandidateId(int candidateId) {
        return jobApplicationsRepo.findByCandidate_Id(candidateId);
    }

    @Override
    public List<JobApplications> getJobApplicationsByMarketingMemberId(int memberId) {
        return jobApplicationsRepo.findByMarketingMember_Id(memberId);
    }

    @Override
    public JobApplications updateJobApplicationStatus(int jobId, Status newStatus) {
        JobApplications jobApplications = jobApplicationsRepo.findById(jobId).orElse(null);
        if (jobApplications != null) {
            jobApplications.setStatus(newStatus);
            jobApplicationsRepo.save(jobApplications);
        }
        return jobApplications;
    }
    @Override
    public JobApplications updateJobApplication(JobApplications jobApplication) {
        return jobApplicationsRepo.save(jobApplication);
    }
    @Override
    public List<JobApplications> getAllJobDetails() {
    	return jobApplicationsRepo.findAll();
    }
}
