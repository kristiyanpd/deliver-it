package com.team9.deliverit.controllers;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.StatusNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ShipmentDisplayDto;
import com.team9.deliverit.models.dtos.ShipmentDto;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.mappers.ShipmentModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getAll(user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ShipmentDisplayDto getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getById(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<ShipmentDisplayDto> filter(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<Integer> warehouseId,
                                           Optional<Integer> userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.filter(user, warehouseId, userId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ShipmentDisplayDto create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ShipmentDto shipmentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Shipment shipment = modelMapper.fromDto(shipmentDto);
            service.create(user, shipment);
            return ShipmentModelMapper.toShipmentDto(shipment);
        } catch (DuplicateEntityException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | StatusNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ShipmentDisplayDto update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody ShipmentDto shipmentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Shipment shipment = modelMapper.fromDto(shipmentDto, id);
            service.update(user, shipment);
            return ShipmentModelMapper.toShipmentDto(shipment);
        } catch (EntityNotFoundException | IllegalArgumentException | StatusNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.delete(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/on-the-way")
    public int countShipmentsOnTheWay(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.countShipmentsOnTheWay(user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/for-warehouse")
    public ShipmentDisplayDto nextShipmentToArrive(@RequestHeader HttpHeaders headers, @RequestParam int warehouseId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.nextShipmentToArrive(warehouseId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}