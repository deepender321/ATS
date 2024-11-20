package com.leverage.ApplicationServices.model;

import java.util.List;

public class CandidateResponse {
    private Candidate candidate;
    private List<JobApplications> jobApplications;
	
	
	public CandidateResponse( Candidate candidate,List<JobApplications> jobApplications) {
		this.candidate = candidate;
		this.jobApplications = jobApplications;
	}
	public CandidateResponse() {
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
	public void setJobApplications(List<JobApplications> jobApplications) {
		this.jobApplications = jobApplications;
	}
}
