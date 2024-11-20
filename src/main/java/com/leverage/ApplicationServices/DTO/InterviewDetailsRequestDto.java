package com.leverage.ApplicationServices.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewDetailsRequestDto {
    private String result;
    private String questions;
    private LocalDateTime scheduledDate;
    private int candidateId;
    private int marketingMemberId;
    private int resumeId;



}
