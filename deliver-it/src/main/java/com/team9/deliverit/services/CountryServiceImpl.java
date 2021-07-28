package com.team9.deliverit.services;

import com.team9.deliverit.models.Country;
import com.team9.deliverit.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private CountryRepository repository;

    @Autowired
    public CountryServiceImpl(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Country> getAllCountries() {
        return repository.getAllCountries();
    }

    @Override
    public Country getCountryById(int id) {
        return repository.getCountryById(id);
    }

    @Override
    public Country getCountryByName(String name) {
        return repository.getCountryByName(name);
    }
}
