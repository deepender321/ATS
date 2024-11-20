package com.leverage.ApplicationServices.model;

import java.util.List;

public class MarketingMemberResponse {
	private MarketingMember marketingMember;
    private List<JobApplications> jobApplications;
  
	
	public MarketingMemberResponse(MarketingMember marketingMember, List<JobApplications> jobApplications) {
		this.marketingMember = marketingMember;
		this.jobApplications = jobApplications;
	}
	public MarketingMemberResponse() {
		
	}
	
	public MarketingMember getMarketingMember() {
		return marketingMember;
	}
	public void setMarketingMember(MarketingMember marketingMember) {
		this.marketingMember = marketingMember;
	}
	public List<JobApplications> getJobApplications() {
		return jobApplications;
	}
	public void setJobApplications(List<JobApplications> jobApplications) {
		this.jobApplications = jobApplications;
	}
	
}
