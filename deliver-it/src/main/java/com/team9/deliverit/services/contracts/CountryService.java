package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.User;

import java.util.List;

public interface CountryService {
    List<Country> getAll();

    Country getById(int id);

    List<Country> getByName(String name);

    void create(User user, Country country);

    void update(User user, Country country);

    void delete(User user, int id);
}
