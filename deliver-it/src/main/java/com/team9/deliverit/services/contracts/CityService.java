package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;

import java.util.List;

public interface CityService {
    List<City> getAll();

    City getById(int id);

    List<City> getByName(String name);

    void create(City city, User user);

    void update(City city, User user);

    void delete(int id, User user);
}
