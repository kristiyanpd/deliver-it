package com.team9.deliverit.repositories;

import com.team9.deliverit.models.City;

import java.util.List;

public interface CityRepository {
    List<City> getAllCities();

    City getCityById(int id);

    City getCityByName(String name);
}
