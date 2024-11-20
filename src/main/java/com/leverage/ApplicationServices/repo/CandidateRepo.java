package com.leverage.ApplicationServices.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.leverage.ApplicationServices.model.Candidate;

public interface CandidateRepo extends JpaRepository<Candidate,Integer>{
	Candidate findByUser_Id(Integer userId);

}
