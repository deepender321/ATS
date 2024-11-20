package com.leverage.ApplicationServices.DTO;

import com.leverage.ApplicationServices.model.Candidate;
import com.leverage.ApplicationServices.model.MarketingMember;

import lombok.Data;

@Data
public class JobResponce {
	private int totalJobsApplied;
	
	private int totalJobsRejected;
	
	private int totalInterviewScheduled;

	private int totalInterviewInprogress;
	
	private Candidate candidate;
	
	private MarketingMember marketingMember;

}
