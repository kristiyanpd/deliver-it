package com.team9.deliverit.services.mappers;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.models.*;
import com.team9.deliverit.models.dtos.UserRegistrationDto;
import com.team9.deliverit.services.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserModelMapper {

    private final UserService userService;
    private final RoleService roleService;
    private final CityService cityService;
    private final AddressService addressService;

    @Autowired
    public UserModelMapper(UserService userService,
                           RoleService roleService,
                           CityService cityService,
                           AddressService addressService) {
        this.userService = userService;
        this.roleService = roleService;
        this.cityService = cityService;
        this.addressService = addressService;
    }

    public User fromDto(UserRegistrationDto userDto) {
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserRegistrationDto userDto, int id) {
        User user = userService.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(UserRegistrationDto userRegistrationDto, User user) {
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setRole(roleService.getById(1));

        Address address = new Address();
        String streetName = userRegistrationDto.getStreetName();
        City city = cityService.getById(userRegistrationDto.getCityId());

        try {
            address.setStreetName(streetName);
            address.setCity(city);
            addressService.create(address);
        } catch (DuplicateEntityException e) {
            address = addressService.getDuplicates(streetName, city.getId()).get(0);
        }
        user.setAddress(address);
    }
}
