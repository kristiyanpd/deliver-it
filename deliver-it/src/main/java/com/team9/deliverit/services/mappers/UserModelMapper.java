package com.team9.deliverit.services.mappers;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.*;
import com.team9.deliverit.models.dtos.UserDisplayDto;
import com.team9.deliverit.models.dtos.UserRegistrationDto;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import com.team9.deliverit.repositories.contracts.CityRepository;
import com.team9.deliverit.repositories.contracts.RoleRepository;
import com.team9.deliverit.repositories.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserModelMapper {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public UserModelMapper(UserRepository userRepository,
                           RoleRepository roleRepository,
                           CityRepository cityRepository,
                           AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
    }

    public static UserDisplayDto toUserDto(User user) {
        UserDisplayDto userDisplayDto = new UserDisplayDto();
        String fullName = user.getFirstName() + " " + user.getLastName();
        userDisplayDto.setId(user.getId());
        userDisplayDto.setFullName(fullName);
        userDisplayDto.setEmail(user.getEmail());

        return userDisplayDto;
    }

    public User fromDto(UserRegistrationDto userDto) {
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(UserRegistrationDto userDto, int id) {
        User user = userRepository.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(UserRegistrationDto userRegistrationDto, User user) {
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setRole(roleRepository.getById(1));

        Address address = new Address();
        String streetName = userRegistrationDto.getStreetName();
        City city = cityRepository.getById(userRegistrationDto.getCityId());

        try {
            address = addressRepository.getDuplicates(streetName, city.getId()).get(0);
        } catch (EntityNotFoundException e) {
            address.setStreetName(streetName);
            address.setCity(city);
            addressRepository.create(address);
        }
        user.setAddress(address);

    }
}
