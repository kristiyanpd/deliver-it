package com.team9.deliverit.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginDto {

    @Email
    @NotBlank(message = "Email can't be blank!")
    @Size(min = 2, max = 100, message = "Email length must be between 2 and 100 symbols!")
    private String email;

    @NotBlank(message = "Password can't be blank!")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
