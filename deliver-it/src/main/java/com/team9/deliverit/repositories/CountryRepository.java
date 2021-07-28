package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Country;

import java.util.List;

public interface CountryRepository {
    List<Country> getAllCountries();

    Country getCountryById(int id);

    Country getCountryByName(String name);
}
