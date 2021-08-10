package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    User getById(int id);

    void create(User user);

    void update(User user);

    void delete(int id);

    User getByEmail(String email);

    boolean isDuplicate(String email);

    int countCustomers();

    User registerEmployee(int id);

    List<Parcel> incomingParcels(int userId);

    List<User> searchEverywhere(String param);

    List<User> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName);
}
