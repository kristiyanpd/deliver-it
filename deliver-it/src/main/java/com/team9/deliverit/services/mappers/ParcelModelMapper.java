package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDto;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
import com.team9.deliverit.repositories.contracts.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParcelModelMapper {

    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;
    private final ParcelRepository parcelRepository;

    @Autowired
    public ParcelModelMapper(ShipmentRepository shipmentRepository, UserRepository userRepository, ParcelRepository parcelRepository) {
        this.shipmentRepository = shipmentRepository;
        this.userRepository = userRepository;
        this.parcelRepository = parcelRepository;
    }

    public Parcel fromDto(ParcelDto parcelDto) {
        Parcel parcel = new Parcel();
        dtoToObject(parcelDto, parcel);
        return parcel;
    }

    public Parcel fromDto(ParcelDto parcelDto, int id) {
        Parcel parcel = parcelRepository.getById(id);
        dtoToObject(parcelDto, parcel);
        return parcel;
    }

    private void dtoToObject(ParcelDto parcelDto, Parcel parcel) {
        Shipment shipment = shipmentRepository.getById(parcelDto.getShipmentId());
        User user = userRepository.getById(parcelDto.getUserId());

        parcel.setWeight(parcelDto.getWeight());
        parcel.setCategory(parcelDto.getCategory());
        parcel.setPickUpOption(parcelDto.getPickUpOption());
        parcel.setUser(user);
        parcel.setShipment(shipment);
    }

}