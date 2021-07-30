package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Country;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CountryRepositoryImpl implements CountryRepository {

    private final List<Country> countries;

    public CountryRepositoryImpl() {
        this.countries = new ArrayList<>();
    }

    @Override
    public List<Country> getAllCountries() {
        return countries;
    }

    @Override
    public Country getCountryById(int id) {
        return countries.stream()
                .filter(country -> country.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Country", id));
    }

    @Override
    public Country getCountryByName(String name) {
        return countries.stream()
                .filter(country -> country.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Country", "name", name));
    }
}
