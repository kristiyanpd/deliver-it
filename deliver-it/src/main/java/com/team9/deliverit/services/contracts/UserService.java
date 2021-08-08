package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.User;

import java.util.List;

public interface UserService {
    List<User> getAll(User user);

    User getById(User user, int id);

    void create(User user);

    void update(User userExecuting, User user);

    void delete(User userExecuting, int id);

    User getByEmail(String email);

    User registerEmployee(int id, User user);
}
