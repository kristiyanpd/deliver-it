package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.PersonalDetails;
import com.team9.deliverit.repositories.AddressRepository;
import com.team9.deliverit.repositories.PersonalDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.List;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {

    private final PersonalDetailsRepository repository;

    @Autowired
    public PersonalDetailsServiceImpl(PersonalDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public PersonalDetails getByEmail(@Email String email) {
        return repository.getByEmail(email);
    }

    @Override
    public PersonalDetails getById(int id) {
        return repository.getById(id);
    }


    @Override
    public void create(PersonalDetails details) {
        boolean duplicateExists = true;
        try {
            repository.getByEmail(details.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Person", "email", details.getEmail());
        }
        repository.create(details);
    }

    @Override
    public void update(PersonalDetails details) {
        boolean duplicateExists = true;

        try {
            PersonalDetails existingAddress = repository.getByEmail(details.getEmail());
            if (existingAddress.getId() == details.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Person", "email", details.getEmail());
        }

        repository.update(details);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
