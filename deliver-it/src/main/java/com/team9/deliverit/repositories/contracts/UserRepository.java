package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {

    boolean isDuplicate(String email);

    int countCustomers();

    List<User> searchEverywhere(String param);

    List<User> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName);
}
