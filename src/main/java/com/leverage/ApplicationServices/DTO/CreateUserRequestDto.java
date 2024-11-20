package com.leverage.ApplicationServices.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {
    private UserRequestDto userRequest;
    private JobApplicationsRequestDto jobApplicationRequest;
    private CandidateRequestDto candidateRequest;
}
