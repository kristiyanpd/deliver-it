package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.dtos.AddressDisplayDto;
import com.team9.deliverit.models.dtos.AddressDto;
import com.team9.deliverit.repositories.contracts.CityRepository;
import com.team9.deliverit.services.contracts.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.team9.deliverit.services.mappers.CityModelMapper.toDto;


@Component
public class AddressModelMapper {

    private final AddressService addressService;
    private final CityRepository cityRepository;

    @Autowired
    public AddressModelMapper(AddressService addressService, CityRepository cityRepository) {
        this.addressService = addressService;
        this.cityRepository = cityRepository;
    }

    public static AddressDisplayDto toAddressDto(Address address) {
        AddressDisplayDto addressDisplayDto = new AddressDisplayDto();
        addressDisplayDto.setCityDisplayDto(toDto(address.getCity()));
        addressDisplayDto.setStreetName(address.getStreetName());

        return addressDisplayDto;

    }

    public Address fromDto(AddressDto addressDto) {
        Address address = new Address();
        dtoToObject(addressDto, address);
        return address;
    }

    public Address fromDto(AddressDto cityDto, int id) {
        Address city = addressService.getById(id);
        dtoToObject(cityDto, city);
        return city;
    }

    private void dtoToObject(AddressDto addressDto, Address address) {
        City city = cityRepository.getById(addressDto.getCityId());
        address.setStreetName(addressDto.getStreetName());
        address.setCity(city);
    }

}