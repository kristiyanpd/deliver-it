package com.team9.deliverit.services;

import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.repositories.CityRepository;
import com.team9.deliverit.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private CityRepository repository;

    @Autowired
    public CityServiceImpl(CityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<City> getAllCities() {
        return repository.getAllCities();
    }

    @Override
    public City getCityById(int id) {
        return repository.getCityById(id);
    }

    @Override
    public City getCityByName(String name) {
        return repository.getCityByName(name);
    }
}
