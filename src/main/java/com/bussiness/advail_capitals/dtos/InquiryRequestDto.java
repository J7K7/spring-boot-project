package com.bussiness.advail_capitals.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryRequestDto {
    private String userName;
    private String message;
    private String email;
    private boolean becamePartner;
    private String phoneNumber;
    private boolean isWatch;
}
