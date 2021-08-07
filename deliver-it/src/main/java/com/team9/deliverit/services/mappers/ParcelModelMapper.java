package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDto;
import com.team9.deliverit.services.contracts.ParcelService;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParcelModelMapper {

    private final ShipmentService shipmentService;
    private final UserService userService;
    private final ParcelService parcelService;

    @Autowired
    public ParcelModelMapper(ShipmentService shipmentService, UserService userService, ParcelService parcelService) {
        this.shipmentService = shipmentService;
        this.userService = userService;
        this.parcelService = parcelService;
    }

    public Parcel fromDto(ParcelDto parcelDto) {
        Parcel parcel = new Parcel();
        dtoToObject(parcelDto, parcel);
        return parcel;
    }

    public Parcel fromDto(ParcelDto parcelDto, int id) {
        Parcel parcel = parcelService.getById(id);
        dtoToObject(parcelDto, parcel);
        return parcel;
    }

    private void dtoToObject(ParcelDto parcelDto, Parcel parcel) {
        Shipment shipment = shipmentService.getById(parcelDto.getShipmentId());
        User user = userService.getById(parcelDto.getUserId());

        parcel.setWeight(parcelDto.getWeight());
        parcel.setCategory(parcelDto.getCategory());
        parcel.setPickUpOption(parcelDto.getPickUpOption());
        parcel.setUser(user);
        parcel.setShipment(shipment);
    }

}