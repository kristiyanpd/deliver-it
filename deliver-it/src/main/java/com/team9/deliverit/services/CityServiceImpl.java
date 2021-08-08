package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.CityRepository;
import com.team9.deliverit.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository repository;

    @Autowired
    public CityServiceImpl(CityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<City> getAll() {
        return repository.getAll();
    }

    @Override
    public City getById(int id) {
        return repository.getById(id);
    }

    @Override
    public City getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public void create(City city, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can create cities!");
        }
        boolean duplicateExists = true;

        try {
            repository.getByName(city.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("City", "name", city.getName());
        }

        repository.create(city);
    }

    @Override
    public void update(City city, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can modify cities!");
        }
        boolean duplicateExists = true;

        try {
            City existingCity = repository.getByName(city.getName());
            if (existingCity.getId() == city.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("City", "name", city.getName());
        }

        repository.update(city);
    }

    @Override
    public void delete(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can delete cities!");
        }
        repository.delete(id);
    }
}
