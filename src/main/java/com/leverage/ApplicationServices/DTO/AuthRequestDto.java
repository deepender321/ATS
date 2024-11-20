package com.leverage.ApplicationServices.DTO;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String mailId;
    private String password;
}
