package com.leverage.ApplicationServices.DTO;

import com.leverage.ApplicationServices.enums.Roles;
import com.leverage.ApplicationServices.service.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationsRequestDto {
	
	private String companyName;
	private String role;
	private String applicationPlatform;
	private String Technology;
	private Status status;
	private String description;
	
}
