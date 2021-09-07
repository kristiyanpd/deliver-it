package com.team9.deliverit.models.dtos;

import javax.validation.constraints.NotBlank;

public class PasswordDto {

    @NotBlank(message = "Old Password can't be blank!")
    private String oldPassword;

    @NotBlank(message = "New Password can't be blank!")
    private String newPassword;

    @NotBlank(message = "Confirm New Password can't be blank!")
    private String confirmNewPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
