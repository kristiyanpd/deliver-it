package com.team9.deliverit.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CountryDto {

    @NotBlank(message = "Country name can't be blank!")
    @Size(min = 2, max = 50, message = "Country name length must be between 2 and 50 symbols!")
    private String name;

    public CountryDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}