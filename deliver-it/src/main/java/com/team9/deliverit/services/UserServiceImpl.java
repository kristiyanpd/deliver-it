package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.UserRepository;
import com.team9.deliverit.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.List;

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
    public void update(User userExecuting, User user) {
        if (!userExecuting.isEmployee() && !userExecuting.getEmail().equals(user.getEmail())) {
            throw new UnauthorizedOperationException("Users can only modify their own credentials!");
        }

        boolean duplicateExists = true;
        try {
            repository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists && !userExecuting.getEmail().equals(user.getEmail())) {
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

}
