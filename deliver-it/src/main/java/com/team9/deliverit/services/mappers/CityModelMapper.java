package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.dtos.CityDisplayDto;
import com.team9.deliverit.models.dtos.CityDto;
import com.team9.deliverit.repositories.contracts.CityRepository;
import com.team9.deliverit.repositories.contracts.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityModelMapper {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public CityModelMapper(CityRepository cityRepository, CountryRepository styleRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = styleRepository;
    }

    public static CityDisplayDto toDisplayDto(City city) {
        CityDisplayDto cityDisplayDto = new CityDisplayDto();
        cityDisplayDto.setName(city.getName());
        cityDisplayDto.setCountry(city.getCountry().getName());

        return cityDisplayDto;
    }

    public CityDto toDto(City city) {
        CityDto cityDto = new CityDto();
        cityDto.setName(city.getName());
        cityDto.setCountryId(city.getCountry().getId());
        return cityDto;
    }

    public City fromDto(CityDto cityDto) {
        City city = new City();
        dtoToObject(cityDto, city);
        return city;
    }

    public City fromDto(CityDto cityDto, int id) {
        City city = cityRepository.getById(id);
        dtoToObject(cityDto, city);
        return city;
    }

    private void dtoToObject(CityDto cityDto, City city) {
        Country country = countryRepository.getById(cityDto.getCountryId());
        city.setName(cityDto.getName());
        city.setCountry(country);
    }

}