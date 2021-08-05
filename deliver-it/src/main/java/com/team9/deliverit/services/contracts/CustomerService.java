package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAll();

    Customer getById(int id);

    void create(Customer customer);

    void update(Customer customer);

    void delete(int id);
}
