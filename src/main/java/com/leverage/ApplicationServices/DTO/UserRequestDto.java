package com.leverage.ApplicationServices.DTO;

import com.leverage.ApplicationServices.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String firstName;
	private String lastName;
    private String mail;
    private String password;
    private Roles role;
    private String mobileNumber;
}
