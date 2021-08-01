package com.team9.deliverit.repositories;

import com.team9.deliverit.models.City;

import java.util.List;

public interface CityRepository {
    List<City> getAll();

    City getById(int id);
    City getByName(String name);
}
