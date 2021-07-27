package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.City;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
public class CityRepositoryImpl {

    private final List<City> cities;

    public CityRepositoryImpl() {
        this.cities = new ArrayList<>();
    }

    public List<City> getAllCities() {
        return cities;
    }

    public City getCityById(int id) {
        return cities.stream()
                .filter(city -> city.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("City", id));
    }
}
