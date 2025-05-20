package com.leverage.ApplicationServices.service;

import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.enums.Roles;
import com.leverage.ApplicationServices.model.User;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface

UserService {

	User createUser(User user);

	User updateUser(User user);
	
	User getUserById(Integer userId);

	void deleteUser(Integer userId);

	User getUser(Integer userId);

	List<User> getAllUsers();

	User saveUser(User user);

	List<User> getAllUsersByRole(Roles role);

	ResponseEntity<?> createUserByAdmin(CreateUserRequestDto createUserRequest);

	ResponseEntity<?> updateUserByAdmin(Integer userId, CreateUserRequestDto createUserRequest);

	ResponseEntity<?> getUserByAdmin(Integer userId);

	ResponseEntity<?> deleteUserByAdmin(Integer userId);

	ResponseEntity<?> getJobDetailsByCandidate(int userId);

	ResponseEntity<?> getAllJobDetails();

	ResponseEntity<?> getJobDetailsByMember(int userId);

	ResponseEntity<?> assignMarketingMemberToCandidate(int candidateUserId, int marketingMemberUserId);

};
