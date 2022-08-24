package com.med.connect.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ForgetPassword {

    @NotBlank
    @Email
    private String email;
}
