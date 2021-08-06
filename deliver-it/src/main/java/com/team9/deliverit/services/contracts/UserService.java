package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.User;

import javax.validation.constraints.Email;
import java.util.List;

public interface UserService {
    List<User> getAll();

    User getById(int id);

    void create(User user);

    void update(User user);

    void delete(int id);

    User getByEmail(String email);
}
