package com.team9.deliverit.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class RegisterDto extends LoginDto {

    @NotBlank(message = "Password can't be blank!")
    private String passwordConfirm;

    @NotBlank(message = "First name can't be blank!")
    @Size(min = 2, max = 50, message = "Street name length must be between 2 and 50 symbols!")
    private String firstName;

    @NotBlank(message = "Last name can't be blank!")
    @Size(min = 2, max = 50, message = "Last name length must be between 2 and 50 symbols!")
    private String lastName;

    @NotBlank(message = "Street name can't be blank!")
    @Size(min = 2, max = 100, message = "Street name length must be between 2 and 100 symbols!")
    private String streetName;

    @Positive(message = "City ID must be positive")
    private int cityId;

    public RegisterDto() {

    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
