package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.AddressDto;
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

    public UserRegistrationDto toDto(User user) {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        userRegistrationDto.setEmail(user.getEmail());
        userRegistrationDto.setCityId(user.getAddress().getCity().getId());
        userRegistrationDto.setStreetName(user.getAddress().getStreetName());
        userRegistrationDto.setFirstName(user.getFirstName());
        userRegistrationDto.setLastName(user.getLastName());

        return userRegistrationDto;
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

        if (addressRepository.isDuplicate(streetName, city.getId())) {
            address = addressRepository.getDuplicate(streetName, city.getId()).get(0);
        } else {
            address.setStreetName(streetName);
            address.setCity(city);
            addressRepository.create(address);
        }

        user.setAddress(address);

    }
}
