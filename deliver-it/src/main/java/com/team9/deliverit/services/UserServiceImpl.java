package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
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

    private  final ParcelRepository parcelRepository;

    @Autowired
    public UserServiceImpl(UserRepository repository, RoleRepository roleRepository, ParcelRepository parcelRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.parcelRepository = parcelRepository;
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
        if (!user.isEmployee() && user.getId() != id) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view", "users"));
        }
        return repository.getById(id);
    }

    @Override
    public User getByEmail(@Email String email) {
        return repository.getByField("email", email);
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
        else if (parcelRepository.getAllUserParcels(id).size() != 0){
            throw new IllegalArgumentException(String.format("User with id %s can't be deleted, because he/she has parcels",id));
        }
        repository.delete(id);
    }

    @Override
    public void registerEmployee(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "register new", "employees"));
        }
        User newEmployee = repository.getById(id);
        newEmployee.setRole(roleRepository.getById(2));
        repository.update(newEmployee);
    }

    @Override
    public void removeEmployee(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "register new", "employees"));
        }
        User newEmployee = repository.getById(id);
        newEmployee.setRole(roleRepository.getById(1));
        repository.update(newEmployee);
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
