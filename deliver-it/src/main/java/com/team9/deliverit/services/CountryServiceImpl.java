package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private final CountryRepository repository;

    @Autowired
    public CountryServiceImpl(CountryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Country> getAll() {
        return repository.getAll();
    }

    @Override
    public Country getById(int id) {
        return repository.getById(id);
    }

    @Override
    public Country getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public void create(Country country) {
        boolean duplicateExists = true;

        try {
            repository.getByName(country.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Country", "name", country.getName());
        }

        repository.create(country);
    }

    @Override
    public void update(Country country) {
        boolean duplicateExists = true;

        try {
            Country existingCountry = repository.getByName(country.getName());
            if (existingCountry.getId() == country.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Country", "name", country.getName());
        }

        repository.update(country);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
