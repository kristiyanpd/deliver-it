package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.PersonalDetails;

import javax.validation.constraints.Email;

public interface PersonalDetailsRepository {
    PersonalDetails getById(int id);

    void create(PersonalDetails personalDetails);

    void update(PersonalDetails personalDetails);

    void delete(int id);

    PersonalDetails getByEmail(@Email String email);
}
