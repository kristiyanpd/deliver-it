package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.AddressDisplayDto;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import com.team9.deliverit.repositories.contracts.UserRepository;
import com.team9.deliverit.services.contracts.AddressService;
import com.team9.deliverit.services.mappers.AddressModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Address> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view all", "addresses"));
        }
        return repository.getAll();
    }

    @Override
    public Address getById(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view", "addresses"));
        }
        return repository.getById(id);
    }

    @Override
    public List<Address> getByName(User user, String name) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view", "addresses"));
        }
        return repository.searchByFieldList("streetName", name);
    }

    @Override
    public void create(User user, Address address) {
        if (repository.isDuplicate(address.getStreetName(), address.getCity().getId())) {
            throw new DuplicateEntityException(String.format("Address %s in %s, %s already exists!",
                    address.getStreetName(),address.getCity().getName(),address.getCity().getCountry().getName()));
        }
        repository.create(address);
    }

    @Override
    public void update(User user, Address address) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "modify", "addresses"));
        }
        if (repository.isDuplicate(address.getStreetName(), address.getCity().getId())) {
            throw new DuplicateEntityException(String.format("Address %s in %s, %s already exists!",
                    address.getStreetName(),address.getCity().getName(),address.getCity().getCountry().getName()));
        }
        repository.update(address);
    }

    @Override
    public void delete(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "delete", "addresses"));
        }

        repository.delete(id);
    }
}
