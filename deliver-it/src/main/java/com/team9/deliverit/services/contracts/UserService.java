package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll(User user);

    User getById(User user, int id);

    void create(User user);

    void update(User userExecuting, User user, int id);

    void delete(User userExecuting, int id);

    User getByEmail(String email);

    void registerEmployee(int id, User user);

    int countCustomers();

    List<User> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName, User user);

    List<User> searchEverywhere(String param, User user);
}
