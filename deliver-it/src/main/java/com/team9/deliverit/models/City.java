package com.team9.deliverit.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class City {

    @Positive(message = "ID should be a positive number!")
    private int id;

    @NotBlank(message = "City name can't be blank!")
    @Size(min = 2, max = 50, message = "City name length should be between 2 and 50 symbols!")
    private String name;

    Country country;

    public City(int id, String name, Country country) {
        setId(id);
        setName(name);
        setCountry(country);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}