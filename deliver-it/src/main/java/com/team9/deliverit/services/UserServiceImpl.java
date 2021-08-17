package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.RoleRepository;
import com.team9.deliverit.repositories.contracts.UserRepository;
import com.team9.deliverit.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view all", "users"));
        }
        return repository.getAll();
    }

    @Override
    public User getById(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view", "users"));
        }
        return repository.getById(id);
    }

    @Override
    public User getByEmail(@Email String email) {
        return repository.getByEmail(email);
    }

    @Override
    public void create(User user) {
        if (repository.isDuplicate(user.getEmail())) {
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
        if (repository.isDuplicate(user.getEmail()) && !user.getEmail().equals(userToUpdate.getEmail())) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }
        repository.update(user);
    }

    @Override
    public void delete(User userExecuting, int id) {
        if (!userExecuting.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "delete", "users"));
        }
        repository.delete(id);
    }

    @Override
    public User registerEmployee(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "register new", "employees"));
        }
        User newEmployee = repository.getById(id);
        newEmployee.setRole(roleRepository.getById(2));
        repository.update(newEmployee);
        return newEmployee;
    }

    @Override
    public int countCustomers() {
        return repository.countCustomers();
    }

    @Override
    public List<User> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "search for", "users"));
        }
        return repository.search(email, firstName, lastName);
    }

    @Override
    public List<User> searchEverywhere(String param, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "search for", "users"));
        }
        return repository.searchEverywhere(param);
    }

}
