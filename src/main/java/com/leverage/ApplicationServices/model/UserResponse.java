package com.leverage.ApplicationServices.model;

import java.util.List;

public class UserResponse {

    private User user;
    private Admin admin;
    private MarketingMember marketingMember;
    private Candidate candidate;
    private List<JobApplications> jobApplications;

   
	public UserResponse(User user, Admin admin, MarketingMember marketingMember,List<JobApplications> jobApplications, Candidate candidate) {
        this.user = user;
        this.admin = admin;
        this.marketingMember = marketingMember;
        this.candidate = candidate;
        this.jobApplications =jobApplications;
    }

    public UserResponse() {
	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public MarketingMember getMarketingMember() {
        return marketingMember;
    }

    public void setMarketingMember(MarketingMember marketingMember) {
        this.marketingMember = marketingMember;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }
    public List<JobApplications> getJobApplications() {
		return jobApplications;
	}

	public void setJobApplications(List<JobApplications> jobAppll) {
		this.jobApplications = jobAppll;
	}

    
}
