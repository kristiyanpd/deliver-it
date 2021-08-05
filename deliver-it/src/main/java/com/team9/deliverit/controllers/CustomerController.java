package com.team9.deliverit.controllers;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.dtos.CustomerRegistrationDto;
import com.team9.deliverit.services.contracts.CustomerService;
import com.team9.deliverit.services.mappers.CustomerModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerModelMapper customerModelMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerModelMapper customerModelMapper) {
        this.customerService = customerService;
        this.customerModelMapper = customerModelMapper;
    }

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/search")
    public List<Customer> search(
            @RequestParam(required = false) Optional<String> email, Optional<String> firstName, Optional<String> lastName) {
        return customerService.search(email, firstName, lastName);
    }

    @GetMapping("/incoming_parcels/{customerId}")
    public List<Parcel> incomingParcels(@PathVariable int customerId) {
        return customerService.incomingParcels(customerId);
    }
    @GetMapping("search?everywhere")
    public List<Customer> searchEverywhere(@RequestParam String value){
        return customerService.searchEverywhere(value);
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable int id) {
        try {
            return customerService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }

    @PostMapping
    public Customer create(@Valid @RequestBody CustomerRegistrationDto customerRegistrationDto) {
        try {
            Customer customer = customerModelMapper.fromDto(customerRegistrationDto);
            customerService.create(customer);
            return customer;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

/*    @PutMapping("/{id}")
    public City update(@PathVariable int id, @Valid @RequestBody CityDto cityDto) {
        try {
            City city = modelMapper.fromDto(cityDto, id);
            service.update(city);
            return city;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {

        try {
            service.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }*/

    //TODO CUSTOMER
}