package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;

import java.util.List;

public interface CustomerRepository {
    List<Customer> getAll();

    Customer getById(int id);

    void create(Customer customer);

    void update(Customer customer);

    void delete(int id);

    List<Customer> searchByEmail(String email);

    List<Customer> searchByName(String name);

    List<Parcel> incomingParcels(int customerId);

    List<Customer> searchAllFields(String param);
}
