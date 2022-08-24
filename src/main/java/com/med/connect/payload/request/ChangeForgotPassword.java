package com.med.connect.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ChangeForgotPassword {

    @NotBlank
    @NotNull
    private String password;

    @NotBlank
    @NotNull
    private String conformPassword;

    @NotBlank
    @NotNull
    private String email;

}
