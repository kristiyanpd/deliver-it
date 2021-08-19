package com.team9.deliverit.controllers.rest;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.AddressDisplayDto;
import com.team9.deliverit.models.dtos.AddressDto;
import com.team9.deliverit.services.contracts.AddressService;
import com.team9.deliverit.services.mappers.AddressModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService service;
    private final AddressModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AddressController(AddressService service, AddressModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<AddressDisplayDto> getAll(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAll(user).stream().map(AddressModelMapper::toAddressDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Address getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getById(user, id);
    }

    @GetMapping("/search")
    public List<Address> getByName(@RequestHeader HttpHeaders headers, @RequestParam String name) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getByName(user, name);
    }

    @PostMapping
    public Address create(@RequestHeader HttpHeaders headers, @Valid @RequestBody AddressDto addressDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Address address = modelMapper.fromDto(addressDto);
        service.create(user, address);
        return address;
    }

    @PutMapping("/{id}")
    public Address update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody AddressDto addressDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Address address = modelMapper.fromDto(addressDto, id);
        service.update(user, address);
        return address;
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.delete(user, id);
    }

}