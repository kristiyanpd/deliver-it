package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.AddressDisplayDto;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import com.team9.deliverit.services.contracts.AddressService;
import com.team9.deliverit.services.mappers.AddressModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AddressDisplayDto> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can get all addresses!");
        }
        return repository.getAll().stream().map(AddressModelMapper::toAddressDto)
                .collect(Collectors.toList());
    }

    @Override
    public Address getById(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can get addresses by ID!");
        }
        return repository.getById(id);
    }

    @Override
    public Address getByName(User user, String name) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can get addresses by name!");
        }
        return repository.getByName(name);
    }

    @Override
    public void create(User user, Address address) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can create addresses!");
        }
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
    public void update(User user, Address address) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can modify addresses!");
        }
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

    @Override
    public void delete(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can delete addresses!");
        }
        repository.delete(id);
    }
}
