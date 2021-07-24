package com.team9.deliverit.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class Country {

    @Positive(message = "ID should be a positive number!")
    private int id;

    @NotBlank(message = "Country name can't be blank!")
    @Size(min = 2, max = 50, message = "Country name length should be between 2 and 50 symbols!")
    private String name;

    public Country(int id, String name) {
        setId(id);
        setName(name);
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
}
