package com.team9.deliverit.controllers;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.WarehouseDisplayDto;
import com.team9.deliverit.models.dtos.WarehouseDto;
import com.team9.deliverit.services.contracts.WarehouseService;
import com.team9.deliverit.services.mappers.WarehouseModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService service;
    private final WarehouseModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public WarehouseController(WarehouseService service,
                               WarehouseModelMapper modelMapper,
                               AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<WarehouseDisplayDto> getAll() {
        return service.getAll()
                .stream().map(WarehouseModelMapper::toWarehouseDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Warehouse getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getById(user, id);
    }

    @PostMapping
    public Warehouse create(@RequestHeader HttpHeaders headers, @Valid @RequestBody WarehouseDto warehouseDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Warehouse warehouse = modelMapper.fromDto(warehouseDto);
        service.create(warehouse, user);
        return warehouse;
    }

    @PutMapping("/{id}")
    public Warehouse update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody WarehouseDto warehouseDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Warehouse warehouse = modelMapper.fromDto(warehouseDto, id);
        service.update(warehouse, user);
        return warehouse;
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.delete(id, user);
    }

}