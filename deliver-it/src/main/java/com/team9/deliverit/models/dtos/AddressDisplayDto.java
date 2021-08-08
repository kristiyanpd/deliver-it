package com.team9.deliverit.models.dtos;

public class AddressDisplayDto {


    private String streetName;

    private CityDisplayDto cityDisplayDto;

    public AddressDisplayDto() {

    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public CityDisplayDto getCityDisplayDto() {
        return cityDisplayDto;
    }

    public void setCityDisplayDto(CityDisplayDto cityDisplayDto) {
        this.cityDisplayDto = cityDisplayDto;
    }
}
