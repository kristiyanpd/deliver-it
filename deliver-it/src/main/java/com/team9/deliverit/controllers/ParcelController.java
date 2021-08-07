package com.team9.deliverit.controllers;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.StatusNotFoundException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.dtos.ParcelDto;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.services.contracts.ParcelService;
import com.team9.deliverit.services.mappers.ParcelModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelService parcelService;
    private final ParcelModelMapper modelMapper;

    @Autowired
    public ParcelController(ParcelService parcelService, ParcelModelMapper modelMapper) {
        this.parcelService = parcelService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/filter")
    public List<Parcel> filter(@RequestParam(required = false) Optional<Double> weight, Optional<Integer> userId,
                               Optional<Integer> warehouseId, Optional<Category> category) {
        return parcelService.filter(weight,userId,warehouseId,category);

    }

    @GetMapping("/sort")
    public List<Parcel> sort(@RequestParam(required = false) Optional<String> weight, Optional<String> date) {
        return parcelService.sort(weight,date);
    }

    //TODO SORTING IN ARRAY
    @GetMapping
    public List<Parcel> getAll() {
        return parcelService.getAll();
    }

    @GetMapping("/{id}")
    public Parcel getById(@PathVariable int id) {
        try {
            return parcelService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }

    @PostMapping
    public Parcel create(@Valid @RequestBody ParcelDto parcelDto) {
        try {
            Parcel parcel = modelMapper.fromDto(parcelDto);
            parcelService.create(parcel);
            return parcel;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException | StatusNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Parcel update(@PathVariable int id, @Valid @RequestBody ParcelDto parcelDto) {
        try {
            Parcel parcel = modelMapper.fromDto(parcelDto,id);
            parcelService.update(parcel);
            return parcel;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            parcelService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}