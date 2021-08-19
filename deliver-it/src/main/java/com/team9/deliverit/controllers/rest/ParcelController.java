package com.team9.deliverit.controllers.rest;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelService service;
    private final ParcelModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ParcelController(ParcelService service, ParcelModelMapper modelMapper,
                            AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<ParcelDisplayDto> getAll(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAll(user).stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ParcelDisplayDto getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        return ParcelModelMapper.toParcelDto(service.getById(id, user));
    }

    @PostMapping
    public Parcel create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ParcelDto parcelDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Parcel parcel = modelMapper.fromDto(parcelDto);
        service.create(parcel, user);
        return parcel;
    }

    @PutMapping("/{id}")
    public Parcel update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                         @Valid @RequestBody ParcelDto parcelDto) {
        User user = authenticationHelper.tryGetUser(headers);
        Parcel parcel = modelMapper.fromDto(parcelDto, id);
        service.update(parcel, user);
        return parcel;
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.delete(id, user);
    }

    @GetMapping("/filter")
    public List<ParcelDisplayDto> filter(@RequestHeader HttpHeaders headers,
                                         @RequestParam(required = false) Optional<Double> weight,
                                         Optional<Integer> warehouseId, Optional<Category> category,
                                         Optional<Status> status, Optional<Integer> userId) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.filter(weight, warehouseId, category, status, userId, user)
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @GetMapping("/sort")
    public List<ParcelDisplayDto> sort(@RequestHeader HttpHeaders headers,
                                       @RequestParam(required = false) Optional<String> weight,
                                       Optional<String> date, Optional<Integer> userId) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.sort(weight, date, userId, user).
                stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @GetMapping("/mine")
    public List<ParcelDisplayDto> getAllUserParcels(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAllUserParcels(user)
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @GetMapping("/mine/incoming")
    public List<ParcelDisplayDto> incomingParcels(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.incomingParcels(user)
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @GetMapping("/mine/past")
    public List<ParcelDisplayDto> pastParcels(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.pastParcels(user)
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}/status")
    public String getStatusOfParcel(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getById(id, user).getShipment().getStatus().toString();
    }

    @PutMapping("/{id}/pick-up")
    public ParcelDisplayDto updatePickUpOption(@RequestHeader HttpHeaders headers,
                                               @PathVariable int id, @RequestParam String option) {
        User user = authenticationHelper.tryGetUser(headers);
        Parcel parcel = service.getById(id, user);
        service.updatePickUpOption(user, parcel, option);
        return ParcelModelMapper.toParcelDto(parcel);
    }

    @PutMapping("/{id}/shipment/{shipmentId}")
    public ParcelDisplayDto updateShipment(@RequestHeader HttpHeaders headers,
                                           @PathVariable int id, @PathVariable int shipmentId) {
        User user = authenticationHelper.tryGetUser(headers);
        Parcel parcel = service.getById(id, user);
        service.updateShipment(user, parcel, shipmentId);
        return ParcelModelMapper.toParcelDto(parcel);
    }
}