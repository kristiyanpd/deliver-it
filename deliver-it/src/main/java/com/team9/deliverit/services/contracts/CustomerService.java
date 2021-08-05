package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAll();

    Customer getById(int id);

    void create(Customer customer);

    void update(Customer customer);

    void delete(int id);

    public List<Customer> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName);

    public List<Parcel> incomingParcels(int customerId);

    public List<Customer> searchEverywhere(String param);
}
