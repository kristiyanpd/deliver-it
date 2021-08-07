package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.Status;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll();

    User getById(int id);

    void create(User user);

    void update(User user);

    void delete(int id);

    User getByEmail(String email);

    User registerEmployee(int id, User user);
}
