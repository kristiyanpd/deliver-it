package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.dtos.CountryDto;
import com.team9.deliverit.repositories.contracts.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryModelMapper {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryModelMapper(CountryRepository styleRepository) {
        this.countryRepository = styleRepository;
    }

    public Country fromDto(CountryDto countryDto) {
        Country country = new Country();
        dtoToObject(countryDto, country);
        return country;
    }

    public Country fromDto(CountryDto countryDto, int id) {
        Country country = countryRepository.getById(id);
        dtoToObject(countryDto, country);
        return country;
    }

    public CountryDto toDto(Country country) {
        CountryDto countryDto = new CountryDto();
        countryDto.setName(country.getName());
        return countryDto;
    }

    private void dtoToObject(CountryDto countryDto, Country country) {
        country.setName(countryDto.getName());
    }

}