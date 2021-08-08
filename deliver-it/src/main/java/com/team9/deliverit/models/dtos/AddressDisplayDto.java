package com.team9.deliverit.models.dtos;

public class AddressDisplayDto {


    private String streetName;

    private CityDisplayDto city;

    public AddressDisplayDto() {

    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public CityDisplayDto getCity() {
        return city;
    }

    public void setCity(CityDisplayDto city) {
        this.city = city;
    }
}
