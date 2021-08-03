package com.team9.deliverit.controllers;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.dtos.CityDto;
import com.team9.deliverit.models.dtos.CustomerRegistrationDto;
import com.team9.deliverit.services.CityService;
import com.team9.deliverit.services.CustomerService;
import com.team9.deliverit.services.mappers.CityModelMapper;
import com.team9.deliverit.services.mappers.CustomerModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

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

}