package com.leverage.ApplicationServices.service;

import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.model.Candidate;
import com.leverage.ApplicationServices.model.Resume;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CandidateService {
	Candidate createCandidate(Candidate candidate);

	Candidate updateCandidate(Candidate candidate);

	void deleteCandidate(int candidateId);

	Candidate getCandidateById(int candidateId);

	List<Candidate> getAllCandidates();

	Candidate getCandidateByUserId(Integer userId);

	//Resume uploadResume(int userId, Resume resume);

	void deleteResume(int resumeId);

	List<Resume> getResumesByCandidateId(int candidateId);

	ResponseEntity<?> updateUserByCandidate(Integer userId, CreateUserRequestDto createUserRequestDto);

	ResponseEntity<?> uploadResume(int userId, MultipartFile[] files);

	ResponseEntity<?> getResume(int resumeId);

	ResponseEntity<?> getCandidateDetailsByUserId(int userId);

	List<Resume> getAllResumes();
	
	ResponseEntity<?> getResumeByCandidateId(int candidateId);

	ResponseEntity<byte[]> getAllResumesByCandidateId(int candidateId);

	ResponseEntity<List<String>> getAllResumesByCandidateIdBylinks(int candidateId);

	Candidate saveCandidate(Candidate candidate);
}
