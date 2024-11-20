package com.leverage.ApplicationServices.service;

import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.DTO.StatusUpdateDto;
import com.leverage.ApplicationServices.model.MarketingMember;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface MarketingMemberService {
	MarketingMember createMarketingMember(MarketingMember marketingMember);

	MarketingMember updateMarketingMember(MarketingMember marketingMember);

	void deleteMarketingMember(int memberId);

	MarketingMember getMarketingMemberById(int memberId);

	List<MarketingMember> getAllMarketingMembers();

	MarketingMember getMarketingMemberByUserId(Integer userId);

	ResponseEntity<?> updateJobApplicationStatus(int jobId, StatusUpdateDto statusUpdate);

	ResponseEntity<?> updateUserByMember(Integer userId, CreateUserRequestDto updatedUserRequest);

	ResponseEntity<?> getMemberDetailsByUserId(int userId);

}
