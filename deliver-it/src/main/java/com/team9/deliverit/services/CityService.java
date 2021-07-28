package com.team9.deliverit.services;

import com.team9.deliverit.models.City;

import java.util.List;

public interface CityService {
    List<City> getAllCities();

    City getCityById(int id);

    City getCityByName(String name);
}
