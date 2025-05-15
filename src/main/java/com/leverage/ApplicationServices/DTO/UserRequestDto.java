//package com.leverage.ApplicationServices.DTO;
//
//import com.leverage.ApplicationServices.enums.Roles;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserRequestDto {
//    private String firstName;
//	private String lastName;
//    private String mail;
//    private String password;
//    private Roles role;
//    private String mobileNumber;
//}
package com.leverage.ApplicationServices.DTO;

import com.leverage.ApplicationServices.enums.Roles;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserRequestDto {

//    @NotNull(message = "First name is required")
//    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain letters only")


    @NotNull(message = "First name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain letters only")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain letters only")
    private String lastName;

    @NotNull(message = "Email is required")
    @Pattern(
            regexp = "^[A-Za-z0 -9._%+-]+@[A-Za-z0-9.-]+\\.com$",
            message = "Email must be valid and end with .com"
    )
    private String mail;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private Roles role;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$" , message = "Phone number must be 10 digits")
    private String mobileNumber;

}
