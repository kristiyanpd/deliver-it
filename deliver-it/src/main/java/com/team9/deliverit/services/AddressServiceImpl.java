package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import com.team9.deliverit.services.contracts.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Address> getAll() {
        return repository.getAll();
    }

    @Override
    public Address getById(int id) {
        return repository.getById(id);
    }

    @Override
    public Address getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public void create(Address address) {
        boolean duplicateExists = true;
        try {
            repository.getDuplicates(address.getStreetName(), address.getCity().getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Address", "street name", address.getStreetName());
        }

        repository.create(address);
    }

    @Override
    public void update(Address address) {
        boolean duplicateExists = true;
        try {
            repository.getDuplicates(address.getStreetName(), address.getCity().getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Address", "street name", address.getStreetName());
        }

        repository.update(address);
    }

    public List<Address> getDuplicates(String name,int cityId){
       return repository.getDuplicates(name,cityId);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
