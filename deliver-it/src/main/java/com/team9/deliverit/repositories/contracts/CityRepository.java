package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.City;

import java.util.List;

public interface CityRepository {
    List<City> getAll();

    City getById(int id);

    List<City> getByName(String name);

    List<City> getDuplicates(String name, int countryId);

    void create(City city);

    void update(City city);

    void delete(int id);
}
