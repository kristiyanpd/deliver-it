package com.team9.deliverit.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CityDto {

    @NotBlank(message = "City name can't be blank!")
    @Size(min = 2, max = 50, message = "City name length should be between 2 and 50 symbols!")
    private String name;

    @Positive(message = "Country ID should be positive!")
    private int countryId;

    public CityDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}