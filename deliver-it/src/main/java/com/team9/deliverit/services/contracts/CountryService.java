package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAll();

    Country getById(int id);

    Country getByName(String name);

    void create(Country country);

    void update(Country country);

    void delete(int id);
}
