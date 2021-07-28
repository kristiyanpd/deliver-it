package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl {

    private final List<Customer> customers;

    public CustomerRepositoryImpl() {
        customers = new ArrayList<>();
    }

    public List<Customer> getAll() {
        return customers;
    }

    public Customer getById(int id) {
        return customers
                .stream()
                .filter(customer -> customer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Customer", id));
    }

    public void create(Customer customer) {
        customers.add(customer);
    }

    public Customer update(Customer customer) {
        Customer customerToUpdate = getById(customer.getId());

        customerToUpdate.getPerson().setFirstName(customer.getPerson().getFirstName());
        customerToUpdate.getPerson().setLastName(customer.getPerson().getLastName());
        customerToUpdate.getPerson().setEmail(customer.getPerson().getEmail());

        return customer;
    }

    public void delete(int id){
        Customer customerToDelete = getById(id);
        customers.remove(customerToDelete);
    }
}
