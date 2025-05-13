package com.leverage.ApplicationServices.DTO;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @Valid
    private UserRequestDto userRequest;

    private JobApplicationsRequestDto jobApplicationRequest;

    private CandidateRequestDto candidateRequest;
}

