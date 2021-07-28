package com.team9.deliverit.services;

import com.team9.deliverit.models.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();

    Country getCountryById(int id);

    Country getCountryByName(String name);
}
