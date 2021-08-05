package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.City;

import java.util.List;

public interface CityService {
    List<City> getAll();

    City getById(int id);

    City getByName(String name);

    void create(City city);

    void update(City city);

    void delete(int id);
}
