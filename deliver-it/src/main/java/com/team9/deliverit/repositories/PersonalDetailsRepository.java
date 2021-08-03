package com.team9.deliverit.repositories;

import com.team9.deliverit.models.PersonalDetails;

public interface PersonalDetailsRepository {
    PersonalDetails getById(int id);

    void create(PersonalDetails personalDetails);

    void update(PersonalDetails personalDetails);

    void delete(int id);
}
