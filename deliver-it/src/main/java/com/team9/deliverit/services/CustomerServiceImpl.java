package com.team9.deliverit.services;

import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.repositories.contracts.CustomerRepository;
import com.team9.deliverit.services.contracts.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Customer> getAll() {
        return repository.getAll();
    }

    @Override
    public Customer getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Customer customer) {
        repository.create(customer);
    }

    @Override
    public void update(Customer customer) {
        repository.update(customer);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }

    @Override
    public List<Customer> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName){
        return repository.search(email,firstName,lastName);
    }

    @Override
    public List<Parcel> incomingParcels(int customerId) {
        return repository.incomingParcels(customerId);
    }

    @Override
    public List<Customer> searchEverywhere(String param) {
        return repository.searchEverywhere(param);
    }

}
