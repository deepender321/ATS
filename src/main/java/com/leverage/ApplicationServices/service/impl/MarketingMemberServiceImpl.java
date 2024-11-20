package com.leverage.ApplicationServices.service.impl;

import com.leverage.ApplicationServices.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.DTO.JobApplicationsRequestDto;
import com.leverage.ApplicationServices.DTO.StatusUpdateDto;
import com.leverage.ApplicationServices.DTO.UserRequestDto;
import com.leverage.ApplicationServices.model.Candidate;
import com.leverage.ApplicationServices.model.JobApplications;
import com.leverage.ApplicationServices.model.MarketingMember;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.repo.MarketingMemberRepo;
import com.leverage.ApplicationServices.service.CandidateService;
import com.leverage.ApplicationServices.service.JobApplicationsService;
import com.leverage.ApplicationServices.service.MarketingMemberService;
import com.leverage.ApplicationServices.service.UserService;
import java.util.List;

@Service
public class MarketingMemberServiceImpl implements MarketingMemberService {

    private final MarketingMemberRepo marketingMemberRepo;
    private final JobApplicationsService jobApplicationsService;
    
    @Lazy
    @Autowired
    private CandidateService candidateService;

    @Lazy
    @Autowired
    private UserService userService;

    public MarketingMemberServiceImpl(MarketingMemberRepo marketingMemberRepo,
                                      JobApplicationsService jobApplicationsService) {
        this.marketingMemberRepo = marketingMemberRepo;
        this.jobApplicationsService = jobApplicationsService;
    }

    @Override
    @Transactional
    public MarketingMember createMarketingMember(MarketingMember marketingMember) {
        return marketingMemberRepo.save(marketingMember);
    }

    @Override
    @Transactional
    public MarketingMember updateMarketingMember(MarketingMember marketingMember) {
        return marketingMemberRepo.save(marketingMember);
    }

    @Override
    @Transactional
    public void deleteMarketingMember(int memberId) {
        marketingMemberRepo.deleteById(memberId);
    }

    @Override
    public MarketingMember getMarketingMemberById(int memberId) {
        return marketingMemberRepo.findById(memberId).orElse(null);
    }

    @Override
    public List<MarketingMember> getAllMarketingMembers() {
        return marketingMemberRepo.findAll();
    }

    @Override
    public MarketingMember getMarketingMemberByUserId(Integer userId) {
    	MarketingMember member = marketingMemberRepo.findByUser_Id(userId);
    	if(member ==null) {
    		ResponseEntity.status(HttpStatus.NOT_FOUND).body("MarketingMember not find with userid "+userId);
    	}
        return member;
    }

    @Override
    public ResponseEntity<?> updateUserByMember(Integer userId, CreateUserRequestDto updatedRequest) {
        User existingUser = userService.getUser(userId);
        UserRequestDto updatedUserRequest = updatedRequest.getUserRequest();
        JobApplicationsRequestDto jobApplicationRequest = updatedRequest.getJobApplicationRequest();
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        if (existingUser.getRoles()!= Roles.ADMIN) {
        existingUser.setFirstName(updatedUserRequest.getFirstName());
        existingUser.setLastName(updatedUserRequest.getLastName());
        existingUser.setMail(updatedUserRequest.getMail());
        existingUser.setPassword(updatedUserRequest.getPassword());
        existingUser.setRoles(updatedUserRequest.getRole());
        existingUser.setMobileNumber(updatedUserRequest.getMobileNumber());
        userService.updateUser(existingUser);
        MarketingMember marketingMember = getMarketingMemberByUserId(userId);
        if (marketingMember != null) {
            updateMarketingMember(marketingMember);
        }
        Candidate candidate = candidateService.getCandidateByUserId(userId);
        candidate.setMarketingMember(marketingMember);
        if(candidate != null) {
        candidateService.updateCandidate(candidate);
        JobApplications jobAppl = new JobApplications();
        jobAppl.setMarketingMember( marketingMember);
        jobAppl.setCandidate(candidate);
        jobAppl.setCompanyName(jobApplicationRequest.getCompanyName());
        jobAppl.setRole(jobApplicationRequest.getRole());
        jobAppl.setStatus(jobApplicationRequest.getStatus());
        jobAppl.setTechnology(jobApplicationRequest.getTechnology());
        jobAppl.setDescription(jobApplicationRequest.getDescription());
        jobAppl.setApplicationPlatform(jobApplicationRequest.getApplicationPlatform());
        jobApplicationsService.saveOrUpdateJobApplication(jobAppl);
        }
        }
        return ResponseEntity.ok("User and MarketingMember updated successfully.");
        
    }

    @Override
    public ResponseEntity<?> getMemberDetailsByUserId(int userId) {
        MarketingMember marketingMember = getMarketingMemberByUserId(userId);
        if (marketingMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("marketingMember not found with user id"+userId);
        }
        User user = marketingMember.getUser();
        if (user == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found with user id"+userId);
        }
        List<JobApplications> jobApplications = jobApplicationsService.getJobApplicationsByMarketingMemberId(marketingMember.getId());
        return ResponseEntity.ok(jobApplications);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateJobApplicationStatus(int jobId, StatusUpdateDto statusUpdate) {
        JobApplications jobApplication = jobApplicationsService.getJobApplicationById(jobId);
        if (jobApplication == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job application not found");
        }

        Candidate candidate = candidateService.getCandidateById(jobApplication.getCandidate().getId());
        if (candidate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found");
        }

        jobApplication.setStatus(statusUpdate.getStatus());
        JobApplications updatedJobApplication = jobApplicationsService.updateJobApplication(jobApplication);

        return ResponseEntity.ok(updatedJobApplication);
    }
}
