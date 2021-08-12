package com.team9.deliverit.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class AddressDto {

    @NotBlank(message = "Street name can't be blank!")
    @Size(min = 2, max = 100, message = "Street name length must be between 2 and 100 symbols!")
    private String streetName;

    @Positive(message = "City ID must be positive!")
    private int cityId;

    public AddressDto() {

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
