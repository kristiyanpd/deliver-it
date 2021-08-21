package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.dtos.AddressDisplayDto;
import com.team9.deliverit.models.dtos.AddressDto;
import com.team9.deliverit.models.dtos.CityDto;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import com.team9.deliverit.repositories.contracts.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.team9.deliverit.services.mappers.CityModelMapper.toDisplayDto;


@Component
public class AddressModelMapper {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;

    @Autowired
    public AddressModelMapper(AddressRepository addressRepository, CityRepository cityRepository) {
        this.addressRepository = addressRepository;
        this.cityRepository = cityRepository;
    }

    public static AddressDisplayDto toAddressDto(Address address) {
        AddressDisplayDto addressDisplayDto = new AddressDisplayDto();
        addressDisplayDto.setCity(toDisplayDto(address.getCity()));
        addressDisplayDto.setStreetName(address.getStreetName());

        return addressDisplayDto;
    }

    public AddressDto toDto(Address address) {
        AddressDto addressDto = new AddressDto();
        addressDto.setStreetName(address.getStreetName());
        addressDto.setCityId(address.getCity().getId());

        return addressDto;
    }

    public Address fromDto(AddressDto addressDto) {
        Address address = new Address();
        dtoToObject(addressDto, address);
        return address;
    }

    public Address fromDto(AddressDto cityDto, int id) {
        Address city = addressRepository.getById(id);
        dtoToObject(cityDto, city);
        return city;
    }

    private void dtoToObject(AddressDto addressDto, Address address) {
        City city = cityRepository.getById(addressDto.getCityId());
        address.setStreetName(addressDto.getStreetName());
        address.setCity(city);
    }

}