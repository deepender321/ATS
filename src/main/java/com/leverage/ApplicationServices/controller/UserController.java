package com.leverage.ApplicationServices.controller;

import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.enums.Roles;
import com.leverage.ApplicationServices.Exception.Exception.EmailAlreadyExistsException;
import com.leverage.ApplicationServices.Exception.Exception.MobileNumberAlreadyExistsException;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/create")
	public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequest) {
		try {
            userService.createUserByAdmin(createUserRequest);
            log.info("creating a new user" ,createUserRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (EmailAlreadyExistsException e) {
        	log.error("Email already exist exception",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (MobileNumberAlreadyExistsException e) {
        	log.error("Mobile Number already exist exception",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
        	log.error("other exceptions while creating user",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
	}

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
