package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.City;

public interface CityRepository extends BaseRepository<City> {

    boolean isDuplicate(String name, int countryId);
}
