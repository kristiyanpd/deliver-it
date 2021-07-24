package com.team9.deliverit.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class Address {

    @Positive(message = "ID should be a positive number!")
    private int id;

    @NotBlank(message = "Street name can't be blank!")
    @Size(min = 2, max = 50, message = "Street name length should be between 2 and 50 symbols!")
    private String streetName;

    City city;

    public Address(int id, String streetName, City city) {
        setId(id);
        setStreetName(streetName);
        setCity(city);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
