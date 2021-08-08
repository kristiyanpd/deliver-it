package com.team9.deliverit.models.dtos;

public class CityDisplayDto {

    private String cityName;

    private String countryName;

    public CityDisplayDto() {

    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
