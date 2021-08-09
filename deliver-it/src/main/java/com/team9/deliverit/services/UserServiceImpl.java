package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDisplayDto;
import com.team9.deliverit.repositories.contracts.UserRepository;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.ParcelModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view all users!");
        }
        return repository.getAll();
    }

    @Override
    public User getById(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view users by ID!");
        }
        return repository.getById(id);
    }

    @Override
    public void create(User user) {
        boolean duplicateExists = true;
        try {
            repository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        repository.create(user);
    }

    @Override
    public void update(User userExecuting, User user, int id) {
        if (!userExecuting.isEmployee() && userExecuting.getId() != id) {
            throw new UnauthorizedOperationException("Users can only modify their own credentials!");
        }

        User userToUpdate = repository.getById(user.getId());
        boolean duplicateExists = true;
        try {
            repository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists && !user.getEmail().equals(userToUpdate.getEmail())) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        repository.update(user);
    }

    @Override
    public void delete(User userExecuting, int id) {
        if (!userExecuting.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can delete users!");
        }
        repository.delete(id);
    }

    @Override
    public User getByEmail(@Email String email) {
        return repository.getByEmail(email);
    }

    @Override
    public User registerEmployee(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can register new employees!");
        }
        return repository.registerEmployee(id);
    }

    @Override
    public int countCustomers() {
        return repository.countCustomers();
    }

    @Override
    public List<User> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can search for users");
        }
        return repository.search(email, firstName, lastName);
    }

    @Override
    public List<ParcelDisplayDto> incomingParcels(int userId, User user) {
        if (!user.isEmployee()) {
            return repository.incomingParcels(user.getId())
                    .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
        }
        return repository.incomingParcels(userId)
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @Override
    public List<User> searchEverywhere(String param, User user) {
        if (!user.isEmployee()){
            throw new UnauthorizedOperationException("Only employees can search for users");
        }
        return repository.searchEverywhere(param);
    }

}
