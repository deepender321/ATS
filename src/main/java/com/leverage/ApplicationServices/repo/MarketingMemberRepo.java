package com.leverage.ApplicationServices.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leverage.ApplicationServices.model.MarketingMember;

public interface MarketingMemberRepo extends JpaRepository<MarketingMember, Integer> {
	
	MarketingMember findByUser_Id(Integer userId);

	}
