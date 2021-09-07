package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.RegisterDto;
import com.team9.deliverit.models.dtos.UserDisplayDto;
import com.team9.deliverit.models.dtos.UserDto;
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

    public static UserDisplayDto toUserDisplayDto(User user) {
        UserDisplayDto userDisplayDto = new UserDisplayDto();
        String fullName = user.getFirstName() + " " + user.getLastName();
        userDisplayDto.setId(user.getId());
        userDisplayDto.setFullName(fullName);
        userDisplayDto.setEmail(user.getEmail());

        return userDisplayDto;
    }

    public RegisterDto toDto(User user) {
        RegisterDto registerDto = new RegisterDto();

        registerDto.setEmail(user.getEmail());
        registerDto.setCityId(user.getAddress().getCity().getId());
        registerDto.setStreetName(user.getAddress().getStreetName());
        registerDto.setFirstName(user.getFirstName());
        registerDto.setLastName(user.getLastName());

        return registerDto;
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setCityId(user.getAddress().getCity().getId());
        userDto.setStreetName(user.getAddress().getStreetName());

        return userDto;
    }

    public User fromUserDto(int id, UserDto userDto) {
        User user = userRepository.getById(id);

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        Address address = new Address();
        String streetName = userDto.getStreetName();
        City city = cityRepository.getById(userDto.getCityId());

        if (addressRepository.isDuplicate(streetName, city.getId())) {
            address = addressRepository.getDuplicate(streetName, city.getId()).get(0);
        } else {
            address.setStreetName(streetName);
            address.setCity(city);
            addressRepository.create(address);
        }
        user.setAddress(address);

        return user;
    }

    public User fromDto(RegisterDto userDto) {
        User user = new User();
        dtoToObject(userDto, user);
        return user;
    }

    public User fromDto(RegisterDto userDto, int id) {
        User user = userRepository.getById(id);
        dtoToObject(userDto, user);
        return user;
    }

    private void dtoToObject(RegisterDto registerDto, User user) {
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setRole(roleRepository.getById(1));

        Address address = new Address();
        String streetName = registerDto.getStreetName();
        City city = cityRepository.getById(registerDto.getCityId());

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
