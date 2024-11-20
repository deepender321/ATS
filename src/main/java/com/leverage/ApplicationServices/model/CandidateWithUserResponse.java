package com.leverage.ApplicationServices.model;

public class CandidateWithUserResponse {
	private Candidate candidate;
	private User user;
	private MarketingMember marketingMember;

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public MarketingMember getmarketingMember() {
		return marketingMember;
	}

	public void setmarketingMember(MarketingMember marketingMember) {
		this.marketingMember = marketingMember;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
