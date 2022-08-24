package com.med.connect.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ForgetPasswordOtp {


    private String otp;


    private String email;


    private String newPassword;


    private String conformPassword;

}
