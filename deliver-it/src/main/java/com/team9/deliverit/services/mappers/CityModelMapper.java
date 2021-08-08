package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.dtos.CityDisplayDto;
import com.team9.deliverit.models.dtos.CityDto;
import com.team9.deliverit.repositories.contracts.CountryRepository;
import com.team9.deliverit.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityModelMapper {

    private final CityService cityService;
    private final CountryRepository countryRepository;

    @Autowired
    public CityModelMapper(CityService cityService, CountryRepository styleRepository) {
        this.cityService = cityService;
        this.countryRepository = styleRepository;
    }

    public static CityDisplayDto toDto(City city) {
        CityDisplayDto cityDisplayDto = new CityDisplayDto();
        cityDisplayDto.setCityName(city.getName());
        cityDisplayDto.setCountryName(city.getCountry().getName());

        return cityDisplayDto;
    }

    public City fromDto(CityDto cityDto) {
        City city = new City();
        dtoToObject(cityDto, city);
        return city;
    }

    public City fromDto(CityDto cityDto, int id) {
        City city = cityService.getById(id);
        dtoToObject(cityDto, city);
        return city;
    }

    private void dtoToObject(CityDto cityDto, City city) {
        Country country = countryRepository.getById(cityDto.getCountryId());
        city.setName(cityDto.getName());
        city.setCountry(country);
    }

}