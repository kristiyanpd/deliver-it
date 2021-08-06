package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User getById(int id);

    void create(User user);

    void update(User user);

    void delete(int id);

    User getByEmail(String email);
}
