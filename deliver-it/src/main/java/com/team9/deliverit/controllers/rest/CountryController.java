package com.team9.deliverit.controllers.rest;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.CountryDto;
import com.team9.deliverit.services.contracts.CountryService;
import com.team9.deliverit.services.mappers.CountryModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryService service;
    private final CountryModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CountryController(CountryService service, CountryModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Country> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Country getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    public List<Country> searchByName(@RequestParam String name) {
        return service.searchByName(name);
    }

    @PostMapping
    public Country create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CountryDto countryDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Country country = modelMapper.fromDto(countryDto);
        service.create(user, country);
        return country;
    }

    @PutMapping("/{id}")
    public Country update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody CountryDto countryDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Country country = modelMapper.fromDto(countryDto, id);
        service.update(user, country);
        return country;
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.delete(user, id);
    }
}