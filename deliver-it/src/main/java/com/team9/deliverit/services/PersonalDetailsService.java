package com.team9.deliverit.services;

import com.team9.deliverit.models.PersonalDetails;

import javax.validation.constraints.Email;

public interface PersonalDetailsService {
    PersonalDetails getByEmail(@Email String email);

    void create(PersonalDetails details);

    void update(PersonalDetails details);

    void delete(int id);

    PersonalDetails getById(int id);
}
