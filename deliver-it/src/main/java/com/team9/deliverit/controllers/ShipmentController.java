package com.team9.deliverit.controllers;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ShipmentDisplayDto;
import com.team9.deliverit.models.dtos.ShipmentDto;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.mappers.ShipmentModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService service;
    private final ShipmentModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ShipmentController(ShipmentService service, ShipmentModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<ShipmentDisplayDto> getAll(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAll(user).stream().map(ShipmentModelMapper::toShipmentDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ShipmentDisplayDto getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        return ShipmentModelMapper.toShipmentDto(service.getById(user, id));
    }

    @GetMapping("/filter")
    public List<ShipmentDisplayDto> filter(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<Integer> warehouseId,
                                           Optional<Integer> userId) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.filter(user, warehouseId, userId)
                .stream().map(ShipmentModelMapper::toShipmentDto).collect(Collectors.toList());
    }

    @PostMapping
    public ShipmentDisplayDto create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ShipmentDto shipmentDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Shipment shipment = modelMapper.fromDto(shipmentDto);
        service.create(user, shipment);
        return ShipmentModelMapper.toShipmentDto(shipment);
    }

    @PutMapping("/{id}")
    public ShipmentDisplayDto update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody ShipmentDto shipmentDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Shipment shipment = modelMapper.fromDto(shipmentDto, id);
        service.update(user, shipment);
        return ShipmentModelMapper.toShipmentDto(shipment);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.delete(user, id);
    }

    @GetMapping("/incoming/count")
    public int countShipmentsOnTheWay(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.countShipmentsOnTheWay(user);
    }

    @GetMapping("/incoming/next")
    public ShipmentDisplayDto nextShipmentToArrive(@RequestHeader HttpHeaders headers, @RequestParam int warehouseId) {
        User user = authenticationHelper.tryGetUser(headers);
        return ShipmentModelMapper.toShipmentDto(service.nextShipmentToArrive(warehouseId, user));
    }
}