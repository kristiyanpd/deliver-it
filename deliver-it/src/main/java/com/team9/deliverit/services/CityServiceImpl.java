package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.CityRepository;
import com.team9.deliverit.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

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
    public List<City> getByName(String name) {
        return repository.searchByFieldList("name", name);
    }

    @Override
    public void create(City city, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "create", "cities"));
        }
        if (repository.isDuplicate(city.getName(), city.getCountry().getId())) {
            throw new DuplicateEntityException("City", "name", city.getName());
        }
        repository.create(city);
    }

    @Override
    public void update(City city, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "modify", "cities"));
        }
        if (repository.isDuplicate(city.getName(), city.getCountry().getId())) {
            throw new DuplicateEntityException("City", "name", city.getName());
        }
        repository.update(city);
    }

    @Override
    public void delete(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "delete", "cities"));
        }
        repository.delete(id);
    }
}
