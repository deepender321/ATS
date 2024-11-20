package com.leverage.ApplicationServices.repo;

import com.leverage.ApplicationServices.model.Resume;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepo extends JpaRepository<Resume, Integer> {
	List<Resume> findByCandidate_Id(Integer candidateId);
    // Custom queries if needed
}
