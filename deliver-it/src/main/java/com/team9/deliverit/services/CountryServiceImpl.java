package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.CountryRepository;
import com.team9.deliverit.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

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
    public List<Country> searchByName(String name) {
        return repository.searchByName(name);
    }

    @Override
    public void create(User user, Country country) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "create", "countries"));
        }
        if (repository.isDuplicate(country.getName())) {
            throw new DuplicateEntityException("Country", "name", country.getName());
        }
        repository.create(country);
    }

    @Override
    public void update(User user, Country country) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "modify", "countries"));
        }
        if (repository.isDuplicate(country.getName())) {
            throw new DuplicateEntityException("Country", "name", country.getName());
        }
        repository.update(country);
    }

    @Override
    public void delete(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "delete", "countries"));
        }
        repository.delete(id);
    }
}
