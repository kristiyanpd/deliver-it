package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.PersonalDetails;
import com.team9.deliverit.models.dtos.CustomerRegistrationDto;
import com.team9.deliverit.services.AddressService;
import com.team9.deliverit.services.CityService;
import com.team9.deliverit.services.CustomerService;
import com.team9.deliverit.services.PersonalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerModelMapper {

    private final CustomerService customerService;
    private final PersonalDetailsService personalDetailsService;
    private final CityService cityService;
    private final AddressService addressService;

    @Autowired
    public CustomerModelMapper(CustomerService customerService, PersonalDetailsService personalDetailsService,
                               CityService cityService, AddressService addressService) {
        this.customerService = customerService;
        this.personalDetailsService = personalDetailsService;
        this.cityService = cityService;
        this.addressService = addressService;
    }

    public Customer fromDto(CustomerRegistrationDto customerDto) {
        Customer customer = new Customer();
        dtoToObject(customerDto, customer);
        return customer;
    }

    public Customer fromDto(CustomerRegistrationDto customerDto, int id) {
        Customer customer = customerService.getById(id);
        dtoToObject(customerDto, customer);
        return customer;
    }

    private void dtoToObject(CustomerRegistrationDto customerRegistrationDto, Customer customer) {
        PersonalDetails personalDetails = new PersonalDetails();
        personalDetails.setFirstName(customerRegistrationDto.getFirstName());
        personalDetails.setLastName(customerRegistrationDto.getLastName());
        personalDetails.setEmail(customerRegistrationDto.getEmail());
        personalDetailsService.create(personalDetails);

        Address address = new Address();
        address.setStreetName(customerRegistrationDto.getStreetName());
        address.setCity(cityService.getById(customerRegistrationDto.getCityId()));
        addressService.create(address);

        customer.setAddress(address);
        customer.setPerson(personalDetails);


    }
}
