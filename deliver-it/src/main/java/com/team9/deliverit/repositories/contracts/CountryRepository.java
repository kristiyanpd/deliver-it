package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Country;

public interface CountryRepository extends BaseRepository<Country> {

    boolean isDuplicate(String name);
}
