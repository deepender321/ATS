package com.leverage.ApplicationServices.controller;

import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.enums.Roles;
import com.leverage.ApplicationServices.Exception.Exception.EmailAlreadyExistsException;
import com.leverage.ApplicationServices.Exception.Exception.MobileNumberAlreadyExistsException;
import com.leverage.ApplicationServices.model.Candidate;
import com.leverage.ApplicationServices.model.MarketingMember;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.service.CandidateService;
import com.leverage.ApplicationServices.service.MarketingMemberService;
import com.leverage.ApplicationServices.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	private final UserService userService;

//	private MarketingMemberService marketingMemberService;
//	private CandidateService candidateService;
	public UserController(UserService userService) //,MarketingMemberService marketingMemberService,
						 // CandidateService candidateService)
	{
		this.userService = userService;
	//	this.marketingMemberService = marketingMemberService;
	//	this.candidateService = candidateService;
	}

//	@PostMapping("/create")
//	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequest) {
//		try {
//            userService.createUserByAdmin(createUserRequest);
//            log.info("creating a new user" ,createUserRequest);
//            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
//        } catch (EmailAlreadyExistsException e) {
//        	log.error("Email already exist exception",e.getMessage());
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (MobileNumberAlreadyExistsException e) {
//        	log.error("Mobile Number already exist exception",e.getMessage());
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (Exception e) {
//        	log.error("other exceptions while creating user",e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
//        }
//	}

//	@PostMapping("/create")
//	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequest) {
//		System.out.println(" VALIDATION PASSED — Calling service");
//		return userService.createUserByAdmin(createUserRequest);
//	}


	@PostMapping("/create")
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequest ) {
		//System.out.println(" Validation passed. Mail: " + createUserRequest.getUserRequest().getMail());

		try {
			userService.createUserByAdmin(createUserRequest);
			log.info(" User created successfully: {}", createUserRequest.getUserRequest().getMail());
			return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");

		} catch (EmailAlreadyExistsException e) {
			log.warn(" Email already exists: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

		} catch (MobileNumberAlreadyExistsException e) {
			log.warn(" Mobile number already exists: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

		} catch (Exception e) {
			log.error(" Unexpected error during user creation", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
		}
	}

	@PutMapping("/assign-marketing/{candidateUserId}/to/{marketingMemberUserId}")
	public ResponseEntity<?> assignMarketingMemberToCandidate(
			@PathVariable int candidateUserId,
			@PathVariable int marketingMemberUserId) {
		return userService.assignMarketingMemberToCandidate(candidateUserId, marketingMemberUserId);
	}

//	@PutMapping("/assign-marketing/{candidateUserId}/to/{marketingMemberUserId}")
//	public ResponseEntity<?> assignMarketingMemberToCandidate(
//			@PathVariable int candidateUserId,
//			@PathVariable int marketingMemberUserId) {
//
//		// This clearly shows you're passing user IDs
//		Candidate candidate = candidateService.getCandidateByUserId(candidateUserId);
//		MarketingMember marketingMember = marketingMemberService.getMarketingMemberByUserId(marketingMemberUserId);
//
//		if (candidate == null || marketingMember == null) {
//			return ResponseEntity.badRequest().body("Invalid candidate or marketing member.");
//		}
//
//		if (candidate.getMarketingMember() != null) {
//			return ResponseEntity.status(409).body("Candidate is already assigned to a marketing member.");
//		}
//
//		candidate.setMarketingMember(marketingMember);
//		candidateService.saveCandidate(candidate);
//
//		return ResponseEntity.ok("Marketing member assigned to candidate.");
//	}

//	@PutMapping("/assign-marketing/{candidateUserId}/to/{marketingMemberUserId}")
//	public ResponseEntity<?> assignMarketingMemberToCandidate(
//			@PathVariable int candidateUserId,
//			@PathVariable int marketingMemberUserId) {
//
//		User candidateUser = userService.getUser(candidateUserId);
//		if (candidateUser == null ) {
//			return ResponseEntity.badRequest().body("Invalid candidate user.");
//		}
//
//		User marketingUser = userService.getUser(marketingMemberUserId);
//		if (marketingUser == null) {
//			return ResponseEntity.badRequest().body("Invalid marketing member user.");
//		}
//
//		MarketingMember marketingMember = marketingMemberService.getMarketingMemberByUserId(marketingMemberUserId);
//		if (marketingMember == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Marketing member not found.");
//		}
//
//		Candidate candidate = candidateService.getCandidateByUserId(candidateUserId);
//		if (candidate == null) {
//			candidate = new Candidate();
//			candidate.setUser(candidateUser);
//		}
//
//		candidate.setMarketingMember(marketingMember);
//		candidateService.saveCandidate(candidate);
//
//		return ResponseEntity.ok("Marketing member assigned to candidate successfully.");
//	}




	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Integer userId,
			@RequestBody CreateUserRequestDto createUserRequest) {
		 log.info("updateing user by admin:" ,userId);
		return userService.updateUserByAdmin(userId, createUserRequest);
	}

	@GetMapping("/details/{userId}")
	public ResponseEntity<?> getUser(@PathVariable Integer userId) {
		 log.info("get user details by userId:" ,userId);
		return userService.getUserByAdmin(userId);
	}
	
	@GetMapping("/{userId}")
	public User getUserById(@PathVariable Integer userId) {
		log.info("get user by userId:" ,userId);
		return userService.getUserById(userId);
	}

	@GetMapping("/allUsers")
	public List<User> getAllUsers() {
		log.info("get All users admin");
		return userService.getAllUsers();
	}

	@GetMapping("/roleBasedUsers/{role}")
	public List<User> getUsersByRole(@PathVariable Roles role) {
		log.info("get users by roles",role.toString());
		return userService.getAllUsersByRole(role);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer userId) {
		log.info("delete user by userId:",userId);
		return userService.deleteUserByAdmin(userId);
	}

	@GetMapping("/jobDetailsByCandidate/{userId}")
	public ResponseEntity<?> getJobDetailsByCandidate(@PathVariable Integer userId) {
		log.info("get jobDetailsByCandidate userId:",userId);
		return userService.getJobDetailsByCandidate(userId);
	}

	@GetMapping("/allUserjobDetails")
	public ResponseEntity<?> getAllUserjobDetails() {
		log.info("get allUserjobDetails");
		return userService.getAllJobDetails();
	}

	@GetMapping("/jobDetailsByMember/{userId}")
	public ResponseEntity<?> getJobDetailsByMember(@PathVariable Integer userId) {
		log.info("get jobDetailsByMember on userId:",userId);
		return userService.getJobDetailsByMember(userId);
	}
}
