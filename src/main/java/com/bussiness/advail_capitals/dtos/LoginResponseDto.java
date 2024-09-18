package com.bussiness.advail_capitals.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true) // Enables fluent setters
public class LoginResponseDto {
    private String token;
    private long expiresIn;
}
