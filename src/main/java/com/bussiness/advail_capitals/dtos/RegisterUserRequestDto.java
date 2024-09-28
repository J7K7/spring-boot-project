package com.bussiness.advail_capitals.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDto {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is mandatory")
    private String password;
    
    @NotBlank(message = "Full name is mandatory")
    private String fullName;
    
    @NotBlank(message = "Phone Number is mandatory")
    private String phoneNumber;

    private String userType;
}
