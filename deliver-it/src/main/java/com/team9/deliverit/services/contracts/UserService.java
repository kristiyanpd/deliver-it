package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDisplayDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAll(User user);

    User getById(User user, int id);

    void create(User user);

    void update(User userExecuting, User user, int id);

    void delete(User userExecuting, int id);

    User getByEmail(String email);

    User registerEmployee(int id, User user);

    int countCustomers();

    List<User> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName, User user);

    List<ParcelDisplayDto> incomingParcels(int userId, User user);

    List<User> searchEverywhere(String param, User user);
}
