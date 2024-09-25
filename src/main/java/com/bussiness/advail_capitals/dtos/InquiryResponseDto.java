package com.bussiness.advail_capitals.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryResponseDto {
    private Integer id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String message;
    private boolean becamePartner;
    private boolean isWatch;
}
