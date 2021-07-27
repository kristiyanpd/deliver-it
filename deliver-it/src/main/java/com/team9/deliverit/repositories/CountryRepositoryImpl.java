package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Country;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CountryRepositoryImpl {

    private final List<Country> countries;

    public CountryRepositoryImpl() {
        this.countries = new ArrayList<>();
    }

    public List<Country> getAllCountries() {
        return countries;
    }

    public Country getCountryById(int id) {
        return countries.stream()
                .filter(country -> country.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Country", id));
    }
}
