package com.team9.deliverit.controllers;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.CityDto;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.mappers.CityModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService service;
    private final CityModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CityController(CityService service, CityModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<City> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public City getById(@PathVariable int id) {
        return service.getById(id);
    }

    @GetMapping("/search")
    public List<City> getByName(@RequestParam String name) {
        return service.getByName(name);
    }

    @PostMapping
    public City create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CityDto cityDto) {
        User user = authenticationHelper.tryGetUser(headers);
        City city = modelMapper.fromDto(cityDto);
        service.create(city, user);
        return city;
    }

    @PutMapping("/{id}")
    public City update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody CityDto cityDto) {
        User user = authenticationHelper.tryGetUser(headers);
        City city = modelMapper.fromDto(cityDto, id);
        service.update(city, user);
        return city;
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.delete(id, user);
    }

}