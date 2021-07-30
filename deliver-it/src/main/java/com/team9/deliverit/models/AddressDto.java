package com.team9.deliverit.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AddressDto {

    @NotBlank(message = "Street name can't be blank!")
    @Size(min = 2, max = 50, message = "Street name length should be between 2 and 50 symbols!")
    private String streetName;

//    City city;

    public AddressDto(String streetName, City city) {
        setStreetName(streetName);
//        setCity(city);
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

//    public City getCity() {
//        return city;
//    }
//
//    public void setCity(City city) {
//        this.city = city;
//    }
}
