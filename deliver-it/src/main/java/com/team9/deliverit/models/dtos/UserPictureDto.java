package com.team9.deliverit.models.dtos;

import javax.validation.constraints.Size;

public class UserPictureDto {

    @Size(max = 500, message = "Profile Picture URL length must be maximum 500 symbols!")
    private String profilePicture;

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
