package com.team9.deliverit.controllers;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.*;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDisplayDto;
import com.team9.deliverit.models.dtos.ParcelDto;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.services.contracts.ParcelService;
import com.team9.deliverit.services.mappers.ParcelModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelService service;
    private final ParcelModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ParcelController(ParcelService service, ParcelModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<ParcelDisplayDto> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getAll(user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ParcelDisplayDto getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getById(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public Parcel create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ParcelDto parcelDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Parcel parcel = modelMapper.fromDto(parcelDto);
            service.create(parcel, user);
            return parcel;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | StatusNotFoundException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Parcel update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody ParcelDto parcelDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Parcel parcel = modelMapper.fromDto(parcelDto, id);
            service.update(parcel, user);
            return parcel;
        } catch (EntityNotFoundException e) {
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
            service.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<ParcelDisplayDto> filter(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<Double> weight,
                               Optional<Integer> warehouseId, Optional<Category> category,
                               Optional<Status> status, Optional<Integer> userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.filter(weight, warehouseId, category, status, userId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    //TODO ?sortBy=weight,date,userId // split
    @GetMapping("/sort")
    public List<ParcelDisplayDto> sort(@RequestHeader HttpHeaders headers, @RequestParam(required = false) Optional<String> weight, Optional<String> date, Optional<Integer> userId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.sort(weight, date, userId, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/get-my-parcels")
    public List<ParcelDisplayDto> getAllUserParcels(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getAllUserParcels(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/status")
    public String getStatusOfParcel(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getStatusOfParcel(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/update-pick-up-option")
    public ParcelDisplayDto updatePickUpOption(@RequestHeader HttpHeaders headers, @PathVariable int id, @RequestParam String pickUpOption) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.updatePickUpOption(user, id, pickUpOption);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (StatusCompletedException | StatusAlreadySameException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}