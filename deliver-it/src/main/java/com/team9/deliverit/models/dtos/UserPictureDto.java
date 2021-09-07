package com.team9.deliverit.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserPictureDto {

    @NotBlank(message = "Profile Picture URL can't be blank!")
    @Size(min = 2, max = 500, message = "Profile Picture URL length must be between 2 and 500 symbols!")
    private String profilePicture;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
