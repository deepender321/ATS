package com.leverage.ApplicationServices.service.impl;

import com.leverage.ApplicationServices.enums.Roles;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.leverage.ApplicationServices.DTO.CandidateRequestDto;
import com.leverage.ApplicationServices.DTO.CreateUserRequestDto;
import com.leverage.ApplicationServices.DTO.JobApplicationsRequestDto;
import com.leverage.ApplicationServices.DTO.UserRequestDto;
import com.leverage.ApplicationServices.model.Candidate;
import com.leverage.ApplicationServices.model.JobApplications;
import com.leverage.ApplicationServices.model.Resume;
import com.leverage.ApplicationServices.model.User;
import com.leverage.ApplicationServices.repo.CandidateRepo;
import com.leverage.ApplicationServices.repo.ResumeRepo;
import com.leverage.ApplicationServices.service.CandidateService;
import com.leverage.ApplicationServices.service.JobApplicationsService;
import com.leverage.ApplicationServices.service.UserService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class CandidateServiceImpl implements CandidateService {

	private final CandidateRepo candidateRepo;
	private final ResumeRepo resumeRepo;
	private final JobApplicationsService jobApplicationsService;

	@Lazy
	private final UserService userService;

	public CandidateServiceImpl(CandidateRepo candidateRepo, ResumeRepo resumeRepo,
			JobApplicationsService jobApplicationsService, @Lazy UserService userService) {
		this.candidateRepo = candidateRepo;
		this.resumeRepo = resumeRepo;
		this.jobApplicationsService = jobApplicationsService;
		this.userService = userService;
	}

	@Override
	@Transactional
	public Candidate createCandidate(Candidate candidate) {
		return candidateRepo.save(candidate);
	}

	@Override
	@Transactional
	public Candidate updateCandidate(Candidate candidate) {
		return candidateRepo.save(candidate);
	}

	@Override
	@Transactional
	public void deleteCandidate(int candidateId) {
		candidateRepo.deleteById(candidateId);
	}

	@Override
	public Candidate getCandidateById(int candidateId) {
		return candidateRepo.findById(candidateId).orElse(null);
	}

	@Override
	public List<Candidate> getAllCandidates() {
		return candidateRepo.findAll();
	}

	@Override
	public Candidate getCandidateByUserId(Integer userId) {
		Candidate candidate = candidateRepo.findByUser_Id(userId);
		if (candidate==null) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body("candidate not find with userid "+userId);
		}
		return candidate;
	}

	@Override
	@Transactional
	public void deleteResume(int resumeId) {
		resumeRepo.deleteById(resumeId);
	}

	@Override
	public List<Resume> getResumesByCandidateId(int candidateId) {
		return resumeRepo.findByCandidate_Id(candidateId);
	}

	@Override
	public List<Resume> getAllResumes() {
		return resumeRepo.findAll();
	}

	@Override
	public ResponseEntity<?> updateUserByCandidate(Integer userId, CreateUserRequestDto createUserRequestDto) {
		UserRequestDto updatedUserRequest = createUserRequestDto.getUserRequest();
		JobApplicationsRequestDto jobApplicationRequest = createUserRequestDto.getJobApplicationRequest();
        CandidateRequestDto candidateRequest = createUserRequestDto.getCandidateRequest();
		User existingUser = userService.getUser(userId);
		if (existingUser == null) {
			return ResponseEntity.notFound().build();
		}

		existingUser.setFirstName(updatedUserRequest.getFirstName());
		existingUser.setLastName(updatedUserRequest.getLastName());
		existingUser.setMail(updatedUserRequest.getMail());
		existingUser.setPassword(updatedUserRequest.getPassword());
		existingUser.setRoles(updatedUserRequest.getRole());
		existingUser.setMobileNumber(updatedUserRequest.getMobileNumber());
		userService.updateUser(existingUser);

		Candidate candidate = getCandidateByUserId(userId);
		 if (candidate != null) {
		 updateCandidate(candidate);
	     }
		JobApplications jobAppl = new JobApplications();
		jobAppl.setTechnology(jobApplicationRequest.getTechnology());
		jobAppl.setDescription(jobApplicationRequest.getDescription());
		jobAppl.setMarketingMember(candidate.getMarketingMember());
		jobAppl.setCandidate(candidate);
		jobAppl.setCompanyName(jobApplicationRequest.getCompanyName());
		jobAppl.setRole(jobApplicationRequest.getRole());
		jobAppl.setStatus(jobApplicationRequest.getStatus());
		jobAppl.setApplicationPlatform(jobApplicationRequest.getApplicationPlatform());

		jobApplicationsService.saveOrUpdateJobApplication(jobAppl);

		return ResponseEntity.ok("User and candidate updated successfully.");
	}

	@Override
	public ResponseEntity<?> uploadResume(int userId, MultipartFile[] files) {
	    List<Resume> savedResumes = new ArrayList<>();
	    List<String> errorMessages = new ArrayList<>();

	    for (MultipartFile file : files) {
	        // Validate the file
	        if (file.isEmpty() || (!file.getContentType().equalsIgnoreCase("application/pdf")
	                && !file.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	                && !file.getContentType().equalsIgnoreCase("application/msword"))) {
	            errorMessages.add("Unsupported file type or empty file: " + file.getOriginalFilename());
	            continue; 
	        }
	        try {
	            String fileName = userId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
	            Path filePath = Paths.get("C:/resumes/" + fileName);  // Specify a directory	            
	            Files.createDirectories(filePath.getParent());
	            Files.write(filePath, file.getBytes());
	            // Create a Resume object and save it
	            Resume resume = new Resume();
	            resume.setFileName(file.getOriginalFilename());
	            resume.setFilePath(filePath.toString());
	            resume.setFileData(file.getBytes());
	            resume.setCandidate(getCandidateByUserId(userId));
	            Resume savedResume = resumeRepo.save(resume);
	            savedResumes.add(savedResume);
	        } catch (IOException e) {
	            errorMessages.add("Failed to upload and save resume: " + file.getOriginalFilename() + " - " + e.getMessage());
	        } catch (DataIntegrityViolationException e) {
	            errorMessages.add("Database error for " + file.getOriginalFilename() + ": " + e.getMessage());
	        }
	    }
	    if (!errorMessages.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessages);
	    }

	    return ResponseEntity.ok(savedResumes);
	}

	@Override
	public ResponseEntity<?> getResume(int resumeId) {
		Resume resume = getResumesByCandidateId(resumeId).stream().filter(r -> r.getId() == resumeId).findFirst()
				.orElse(null);

		if (resume == null) {
			return ResponseEntity.notFound().build();
		}

		try {
			Path filePath = Paths.get(resume.getFilePath());
			byte[] fileData = Files.readAllBytes(filePath);
			MediaType contentType;
			String fileExtension = getFileExtension(resume.getFileName());
			switch (fileExtension.toLowerCase()) {
			case "pdf":
				contentType = MediaType.APPLICATION_PDF;
				break;
			case "doc":
			case "docx":
				contentType = MediaType.APPLICATION_OCTET_STREAM;
				break;
			default:
				contentType = MediaType.APPLICATION_OCTET_STREAM;
				break;
			}

			// Return the file as an attachment
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
					.contentType(contentType).body(fileData);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read file");
		}
	}
   //latest resume of candidate
	@Override
	public ResponseEntity<?> getResumeByCandidateId(int candidateId) {
		List<Resume> resumes = getResumesByCandidateId(candidateId);
		if (resumes.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No resumes found for this candidate.");
		}

		Resume latestResume = resumes.stream().max(Comparator.comparing(Resume::getCreatedDate)) .orElse(null);
		if (latestResume == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No resumes found for this candidate.");
		}

		try {
			Path filePath = Paths.get(latestResume.getFilePath());

			if (!Files.exists(filePath)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
			}
			MediaType contentType;
			String fileExtension = getFileExtension(latestResume.getFileName());
			switch (fileExtension.toLowerCase()) {
			case "pdf":
				contentType = MediaType.APPLICATION_PDF;
				break;
			case "doc":
			case "docx":
				contentType = MediaType.APPLICATION_OCTET_STREAM;
				break;
			default:
				contentType = MediaType.APPLICATION_OCTET_STREAM; 
				break;
			}

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + latestResume.getFileName() + "\"")
					.contentType(contentType).body(Files.readAllBytes(filePath));																		
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read file");
		}
	}
	
   //All resumes in zip
	@Override
	public ResponseEntity<byte[]> getAllResumesByCandidateId(int candidateId) {
	    List<Resume> resumes = getResumesByCandidateId(candidateId);

	    if (resumes.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No resumes found for this candidate.".getBytes());
	    }

	    Path zipFilePath;
	    try {
	        zipFilePath = Files.createTempFile("resumes_" + candidateId, ".zip");

	        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
	            for (Resume resume : resumes) {
	                Path filePath = Paths.get(resume.getFilePath());
	                if (Files.exists(filePath)) {
	                    ZipEntry zipEntry = new ZipEntry(resume.getFileName());
	                    zos.putNextEntry(zipEntry);
	                    Files.copy(filePath, zos);
	                    System.out.println(" filePath is :::::"+filePath);
	                    System.out.println("zos is :::::"+zos);
	                    zos.closeEntry();
	                    
	                }
	            }
	        }
	       
	        // Read the ZIP file into a byte array
	        byte[] zipData = Files.readAllBytes(zipFilePath);
              System.out.println("candidate zip data is :::::"+zipData);
	        // Delete the temporary ZIP file after reading it
	        //Files.delete(zipFilePath);

	        // Return the ZIP file as a response
              System.out.println("output iss "+ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resumes_" + candidateId + ".zip\"")
              .contentType(MediaType.APPLICATION_OCTET_STREAM)
              .body(zipData));
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resumes_" + candidateId + ".zip\"")
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(zipData);

	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create ZIP file.".getBytes());
	    }
	}

	//individual files links
	@Override
	public ResponseEntity<List<String>> getAllResumesByCandidateIdBylinks(int candidateId) {
	    List<Resume> resumes = getResumesByCandidateId(candidateId);

	    if (resumes.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	    }

	    List<String> resumeLinks = resumes.stream()
	            .map(resume -> "/resumes/" + resume.getId())
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(resumeLinks);
	}
	//method using to get extension f file
	private String getFileExtension(String fileName) {
	    int lastIndex = fileName.lastIndexOf('.');
	    return (lastIndex > 0) ? fileName.substring(lastIndex + 1) : "";
	}

	@Override
	public ResponseEntity<?> getCandidateDetailsByUserId(int userId) {
		Candidate candidate = getCandidateByUserId(userId);
		if (candidate == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("candidates not found with user id"+userId);
		}

		User user = candidate.getUser();
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found with user id"+userId);
		}
		List<JobApplications> jobApplications = jobApplicationsService
			.getJobApplicationsByCandidateId(candidate.getId());

		return ResponseEntity.ok(jobApplications);
	}
}
