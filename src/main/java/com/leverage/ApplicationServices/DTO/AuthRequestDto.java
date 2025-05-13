//package com.leverage.ApplicationServices.DTO;
//
//import lombok.Data;
//
//@Data
//public class AuthRequestDto {
//    private String mailId;
//    private String password;
//}

package com.leverage.ApplicationServices.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthRequestDto {

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$",
            message = "Email must be valid and end with .com"
    )
    private String mailId;

    @NotBlank(message = "Password is required")
    private String password;
}
