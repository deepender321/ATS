package com.leverage.ApplicationServices.service.impl;

import com.leverage.ApplicationServices.enums.Roles;
import com.leverage.ApplicationServices.DTO.CandidateRequestDto;
import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.DTO.JobApplicationsRequestDto;
import com.leverage.ApplicationServices.DTO.JobResponce;
import com.leverage.ApplicationServices.DTO.UserRequestDto;
import com.leverage.ApplicationServices.Exception.Exception.EmailAlreadyExistsException;
import com.leverage.ApplicationServices.Exception.Exception.MobileNumberAlreadyExistsException;
import com.leverage.ApplicationServices.controller.UserController;
import com.leverage.ApplicationServices.model.Admin;
import com.leverage.ApplicationServices.model.Candidate;
import com.leverage.ApplicationServices.model.JobApplications;
import com.leverage.ApplicationServices.model.MarketingMember;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.repo.UserRepo;
import com.leverage.ApplicationServices.service.AdminService;
import com.leverage.ApplicationServices.service.CandidateService;
import com.leverage.ApplicationServices.service.EmailService;
import com.leverage.ApplicationServices.service.JobApplicationsService;
import com.leverage.ApplicationServices.service.MarketingMemberService;
import com.leverage.ApplicationServices.service.Status;
import com.leverage.ApplicationServices.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final AdminService adminService;
    private final EmailService emailService;
    private final JobApplicationsService jobApplicationsService;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private CandidateService candidateService;

    @Lazy
    @Autowired
    private MarketingMemberService marketingMemberService;

    public UserServiceImpl(UserRepo userRepo, AdminService adminService,
                           EmailService emailService,
                           JobApplicationsService jobApplicationsService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.adminService = adminService;
        this.emailService = emailService;
        this.jobApplicationsService = jobApplicationsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        if (userRepo.existsByMail(user.getMail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (userRepo.existsByMobileNumber(user.getMobileNumber())) {
            throw new MobileNumberAlreadyExistsException("Number already exists");
        }
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (userRepo.findById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }
        return userRepo.save(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (userRepo.existsById(userId)) {
            userRepo.deleteById(userId);
        } else {
            throw new IllegalArgumentException("User does not exist");
        }
    }

    @Override
    public User getUser(Integer userId) {
        return userRepo.findById(userId).orElse(null);
    }

    private boolean doesEmailExist(String email) {
        return userRepo.existsByMail(email);
    }

    @Override
    public User saveUser(User user) {
        if (doesEmailExist(user.getMail())) {
            return null;
        }
        return userRepo.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<User> getAllUsersByRole(Roles role) {
        return userRepo.getUserByRoles(role);
    }

    @Override
    public User getUserById(Integer userId) {
        return userRepo.findById(userId).orElse(null);
    }

//    @Override
//    public ResponseEntity<?> createUserByAdmin(CreateUserRequestDto createUserRequest) {
//        UserRequestDto userRequest = createUserRequest.getUserRequest();
//        CandidateRequestDto candidateRequest = createUserRequest.getCandidateRequest();
//        User user = new User();
//
//        user.setFirstName(userRequest.getFirstName());
//        user.setLastName(userRequest.getLastName());
//        user.setMail(userRequest.getMail());
//        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//        user.setRoles(userRequest.getRole());
//        user.setMobileNumber(userRequest.getMobileNumber());
//
//        String subject = "Welcome to the ATS System";
//        String text = "Hello " + user.getFirstName() + ",\n\nYou have been successfully registered as a candidate.";
//        emailService.sendEmailToCandidate(user.getMail(), userRequest.getPassword(), subject, text);
//
//        User savedUser = null;
//        boolean userCreated = false;
//
//        switch (user.getRoles()) {
//            case ADMIN -> {
//                Admin admin = new Admin();
//                admin.setUser(user);
//                adminService.createAdmin(admin);
//                log.info("created a new Admin");
//                userCreated = true;
//            }
//            case MARKETING -> {
//                MarketingMember marketingMember = new MarketingMember();
//                marketingMember.setUser(user);
//                marketingMemberService.createMarketingMember(marketingMember);
//                log.info("created a new Member");
//                userCreated = true;
//            }
//            case CANDIDATE -> {
//                MarketingMember member = marketingMemberService.getMarketingMemberById(candidateRequest.getMarketingMemberId());
//                if (member == null) {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marketing Member not found.");
//                }
//                log.info("created a new CANDIDATE");
//                userCreated = true;
//            }
//            default -> {
//                return ResponseEntity.badRequest().body("Invalid access level.");
//            }
//        }
//
//        if (userCreated) {
//            savedUser = createUser(user);
//            if (candidateRequest != null) {
//                Candidate candidate = new Candidate();
//                candidate.setUser(savedUser);
//                candidate.setMarketingMember(marketingMemberService.getMarketingMemberById(candidateRequest.getMarketingMemberId()));
//                candidateService.createCandidate(candidate);
//            }
//            log.info("created a new user");
//            return ResponseEntity.status(HttpStatus.OK).body("User created successfully");
//        } else {
//        	  log.error("User creation failed",HttpStatus.INTERNAL_SERVER_ERROR);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User creation failed.");
//        }
//
//
//    }

    @Override
    public ResponseEntity<?> createUserByAdmin(CreateUserRequestDto createUserRequest) {
        try {
            UserRequestDto userRequest = createUserRequest.getUserRequest();
            CandidateRequestDto candidateRequest = createUserRequest.getCandidateRequest();
            JobApplicationsRequestDto jobRequest = createUserRequest.getJobApplicationRequest();

            if (userRequest == null || userRequest.getRole() == null) {
                return ResponseEntity.badRequest().body("Missing userRequest or role.");
            }

            //  Create base User
            User user = new User();
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setMail(userRequest.getMail());
            user.setMobileNumber(userRequest.getMobileNumber());
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            user.setRoles(userRequest.getRole());

            User savedUser = userRepo.save(user);

            //  Optional welcome email
            try {
                String subject = "Welcome to the ATS System";
                String text = "Hello " + user.getFirstName() + ",\n\nYou have been successfully registered.";
                emailService.sendEmailToCandidate(user.getMail(), userRequest.getPassword(), subject, text);
            } catch (Exception e) {
                log.warn(" Failed to send email: {}", e.getMessage());
            }

//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            boolean matches = encoder.matches(userRequest.getPassword(), savedUser.getPassword());
//            if (matches) {
//                log.info("Password matches for user: {}", user.getMail());
//            } else {
//                log.error("Password does not match for user: {}", user.getMail());
//            }

            //  Role handling
            switch (user.getRoles()) {
                case ADMIN -> {
                    if (candidateRequest != null || jobRequest != null) {
                        return ResponseEntity.badRequest().body("ADMIN should not contain candidate/job data.");
                    }
                    Admin admin = new Admin();
                    admin.setUser(savedUser);
                    adminService.createAdmin(admin);
                    log.info(" Created new ADMIN");
                }

                case MARKETING -> {
                    if (candidateRequest != null) {
                        return ResponseEntity.badRequest().body("MARKETING should not contain candidateRequest.");
                    }
                    MarketingMember marketingMember = new MarketingMember();
                    marketingMember.setUser(savedUser);
                    marketingMemberService.createMarketingMember(marketingMember);
                    log.info(" Created new MARKETING MEMBER");
                }

                case CANDIDATE -> {
                    if (candidateRequest == null) {
                        return ResponseEntity.badRequest().body("CANDIDATE must include valid marketingMemberId.");
                    }

                    MarketingMember member = marketingMemberService.getMarketingMemberById(candidateRequest.getMarketingMemberId());
                    if (member == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marketing Member not found.");
                    }

                    Candidate candidate = new Candidate();
                    candidate.setUser(savedUser);
                    candidate.setMarketingMember(member);
                    candidateService.createCandidate(candidate);

                    // (Optional) handle jobRequest here
                    log.info(" Created new CANDIDATE");
                }

                default -> {
                    return ResponseEntity.badRequest().body(" Invalid role specified.");
                }
            }

            //  Save user last
         //   userRepo.save(user);
            log.info(" Final user record saved: {}", user.getMail());

            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            log.error(" Unexpected error during user creation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }


    @Override
    public ResponseEntity<?> updateUserByAdmin(Integer userId, CreateUserRequestDto createUserRequest) {
        UserRequestDto updatedUserRequest = createUserRequest.getUserRequest();
        JobApplicationsRequestDto jobApplicationRequest = createUserRequest.getJobApplicationRequest();
        CandidateRequestDto candidateRequest = createUserRequest.getCandidateRequest();

        User existingUser = getUser(userId);
        if (existingUser == null) {
        	log.error("User not found with userId:",userId);
            return ResponseEntity.notFound().build();
        }

        existingUser.setFirstName(updatedUserRequest.getFirstName());
        existingUser.setLastName(updatedUserRequest.getLastName());
        existingUser.setMail(updatedUserRequest.getMail());
        existingUser.setPassword(passwordEncoder.encode(updatedUserRequest.getPassword()));
        existingUser.setRoles(updatedUserRequest.getRole());
        existingUser.setMobileNumber(updatedUserRequest.getMobileNumber());

        updateUser(existingUser);
        JobApplications jobAppl = new JobApplications();

        switch (existingUser.getRoles()) {
            case ADMIN -> {
            	log.info("User updated successfully");
                return ResponseEntity.ok("User updated successfully.");
            }
            case MARKETING -> {
                MarketingMember marketingMember = marketingMemberService.getMarketingMemberByUserId(userId);
                if (marketingMember != null) {
                    marketingMemberService.updateMarketingMember(marketingMember);
                    jobAppl.setMarketingMember(marketingMember);
                }
                log.info("MarketingMember detials updated successfully");
                return ResponseEntity.ok("User and MarketingMember updated successfully.");
            }
            case CANDIDATE -> {
                Candidate candidate = candidateService.getCandidateByUserId(userId);
                if (candidate != null) {
                    MarketingMember mm = marketingMemberService.getMarketingMemberById(candidateRequest.getMarketingMemberId());
                    if (mm == null) {
                    	log.error("Marketing Member not found with assigned memberId:",candidateRequest.getMarketingMemberId());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marketing Member not found.");
                    }
                    candidate.setMarketingMember(mm);
                    candidateService.updateCandidate(candidate);

                    jobAppl.setTechnology(jobApplicationRequest.getTechnology());
                    jobAppl.setMarketingMember(mm);
                    jobAppl.setCandidate(candidate);
                    jobAppl.setCompanyName(jobApplicationRequest.getCompanyName());
                    jobAppl.setRole(jobApplicationRequest.getRole());
                    jobAppl.setStatus(jobApplicationRequest.getStatus());
                    jobAppl.setApplicationPlatform(jobApplicationRequest.getApplicationPlatform());
                    jobApplicationsService.saveOrUpdateJobApplication(jobAppl);
                }
                log.info("Candidate detials updated successfully");
                return ResponseEntity.status(HttpStatus.OK).body("User and Candidate updated successfully.");
            }
            default -> {
            	log.info("Invalid access level. fro updating the user by admin");
                return ResponseEntity.badRequest().body("Invalid access level.");
            }
        }
    }

    @Override
    public ResponseEntity<?> getUserByAdmin(Integer userId) {
        User user = getUser(userId);
        if (user == null) {
        	log.info("getUserByAdmin User not found with userId:",userId);
            return ResponseEntity.badRequest().body("User not found with id " + userId);
        }

        switch (user.getRoles()) {
            case ADMIN -> {
                Admin admin = adminService.getAdminByUserId(userId);
                return ResponseEntity.ok(admin);
            }
            case MARKETING -> {
                MarketingMember marketingMember = marketingMemberService.getMarketingMemberByUserId(userId);
                List<JobApplications> jobApplList = jobApplicationsService.getJobApplicationsByMarketingMemberId(marketingMember.getId());
                return ResponseEntity.ok(jobApplList);
            }
            case CANDIDATE -> {
                Candidate candidate = candidateService.getCandidateByUserId(userId);
                List<JobApplications> jobApplListForCandidate = jobApplicationsService.getJobApplicationsByCandidateId(candidate.getId());
                return ResponseEntity.ok(jobApplListForCandidate);
            }
            default -> {
            	log.error("Invalid access level at getUserByAdmin ",HttpStatus.NOT_FOUND);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid access level");
            }
        }
    }

    @Override
    public ResponseEntity<?> deleteUserByAdmin(Integer userId) {
        User user = getUser(userId);
        if (user == null) {
        	log.info("deleteUserByAdmin User not found with userId:",userId);
            return ResponseEntity.badRequest().body("User not found with id " + userId);
        }

        switch (user.getRoles()) {
            case ADMIN -> adminService.deleteAdminById(userId);
            case MARKETING -> marketingMemberService.deleteMarketingMember(userId);
            case CANDIDATE -> candidateService.deleteCandidate(userId);
        }

        Candidate candidate = candidateService.getCandidateByUserId(userId);
        jobApplicationsService.deleteJobApplicationByCandidateId(candidate.getId());
        deleteUser(userId);
        log.info("deleteUserByAdmin deleted user successfully with userId:",userId);
        return ResponseEntity.ok("User and related entities deleted successfully.");
    }

    @Override
    public ResponseEntity<?> getJobDetailsByCandidate(int userId) {
        User user = getUser(userId);
        if (user == null) {
        	log.info("getJobDetailsByCandidate User not found with userId:",userId);
            return ResponseEntity.badRequest().body("User does not exist with id " + userId);
        }

        Candidate candidate = candidateService.getCandidateByUserId(userId);
        if (candidate == null) {
        	 log.info("getJobDetailsByCandidate candidate not found with userId:",userId);
            return ResponseEntity.badRequest().body("Candidate does not exist with user id " + userId);
        }

        JobResponce jobResponce = new JobResponce();
        List<JobApplications> jobApplications = jobApplicationsService.getJobApplicationsByCandidateId(candidate.getId());

        if (jobApplications != null) {
            jobResponce.setCandidate(candidate);
            jobResponce.setTotalJobsApplied(jobApplications.size());
            int k = 1, l = 1, m = 1;

            for (JobApplications jobApp : jobApplications) {
                switch (jobApp.getStatus()) {
                    case INPROCESS -> jobResponce.setTotalInterviewInprogress(k++);
                    case SCHEDULED -> jobResponce.setTotalInterviewScheduled(l++);
                    case REJECTED -> jobResponce.setTotalJobsRejected(m++);
                }
            }
        } else {
        	 log.info("getJobDetailsByCandidate no job details for candidate with userId:",userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No job applications for " + userId);
        }
        log.info("getJobDetailsByCandidate with userId:",userId ,"job details are",jobResponce);
        return ResponseEntity.ok(jobResponce);
    }

    @Override
    public ResponseEntity<?> getAllJobDetails() {
        List<Candidate> candidates = candidateService.getAllCandidates();
        List<JobResponce> jobResponses = new ArrayList<>();

        if (candidates != null && !candidates.isEmpty()) {
            for (Candidate candidate : candidates) {
                JobResponce jobResponce = new JobResponce();
                List<JobApplications> jobApplications = jobApplicationsService.getJobApplicationsByCandidateId(candidate.getId());

                if (jobApplications != null) {
                    jobResponce.setCandidate(candidate);
                    jobResponce.setTotalJobsApplied(jobApplications.size());

                    int totalInterviewInProgress = 0;
                    int totalInterviewScheduled = 0;
                    int totalJobsRejected = 0;

                    for (JobApplications jobApplication : jobApplications) {
                        switch (jobApplication.getStatus()) {
                            case INPROCESS -> totalInterviewInProgress++;
                            case SCHEDULED -> totalInterviewScheduled++;
                            case REJECTED -> totalJobsRejected++;
                        }
                    }

                    jobResponce.setTotalInterviewInprogress(totalInterviewInProgress);
                    jobResponce.setTotalInterviewScheduled(totalInterviewScheduled);
                    jobResponce.setTotalJobsRejected(totalJobsRejected);
                }
                jobResponses.add(jobResponce);
            }
            log.info("getAllJobDetails statistics are",jobResponses);
            return ResponseEntity.ok(jobResponses);
        } else {
        	log.info("no candidates registered yet");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No candidates found.");
        }
    }

    @Override
    public ResponseEntity<?> getJobDetailsByMember(int userId) {
        MarketingMember member = marketingMemberService.getMarketingMemberByUserId(userId);
        JobResponce jobResponce = new JobResponce();

        List<JobApplications> jobApplications = jobApplicationsService.getJobApplicationsByMarketingMemberId(member.getId());
        if (jobApplications != null) {
            jobResponce.setMarketingMember(member);
            jobResponce.setTotalJobsApplied(jobApplications.size());

            int totalInterviewInProgress = 0;
            int totalInterviewScheduled = 0;
            int totalJobsRejected = 0;

            for (JobApplications jobApp : jobApplications) {
                switch (jobApp.getStatus()) {
                    case INPROCESS -> totalInterviewInProgress++;
                    case SCHEDULED -> totalInterviewScheduled++;
                    case REJECTED -> totalJobsRejected++;
                }
            }

            jobResponce.setTotalInterviewInprogress(totalInterviewInProgress);
            jobResponce.setTotalInterviewScheduled(totalInterviewScheduled);
            jobResponce.setTotalJobsRejected(totalJobsRejected);
        }
        log.info("getJobDetailsByMember",userId ,"statistics are",jobResponce);
        return ResponseEntity.ok(jobResponce);
    }

    
}
