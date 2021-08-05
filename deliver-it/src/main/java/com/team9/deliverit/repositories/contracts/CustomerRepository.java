package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    List<Customer> getAll();

    Customer getById(int id);

    void create(Customer customer);

    void update(Customer customer);

    void delete(int id);

    public List<Customer> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName);

    List<Parcel> incomingParcels(int customerId);

    List<Customer> searchEverywhere(String param);
}
