package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Country;

import java.util.List;

public interface CountryRepository {
    List<Country> getAll();

    Country getById(int id);

    Country getByName(String name);

    List<Country> searchByName(String name);

    void create(Country Country);

    void update(Country Country);

    void delete(int id);

    List<Country> getDuplicates(String name);
}
