package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Country;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {

    private final List<City> cities;

    public CityRepositoryImpl() {
        this.cities = new ArrayList<>();

        //TODO Add example cities
    }

    @Override
    public List<City> getAllCities() {
        return cities;
    }

    @Override
    public City getCityById(int id) {
        return cities.stream()
                .filter(city -> city.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("City", id));
    }

    @Override
    public City getCityByName(String name) {
        return cities.stream()
                .filter(city -> city.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("City", "name", name));
    }
}
