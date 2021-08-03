package com.team9.deliverit.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CustomerRegistrationDto {

    @NotBlank(message = "First name can't be blank!")
    @Size(min = 2, max = 50, message = "Street name length should be between 2 and 50 symbols!")
    private String firstName;

    @NotBlank(message = "Last name can't be blank!")
    @Size(min = 2, max = 50, message = "Last name length should be between 2 and 50 symbols!")
    private String lastName;

    @Email
    private String email;

    @Positive(message = "Address ID should be positive!")
    private int addressId;

    public CustomerRegistrationDto() {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
