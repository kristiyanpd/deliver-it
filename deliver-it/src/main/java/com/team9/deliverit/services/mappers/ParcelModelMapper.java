package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.ParcelDto;
import com.team9.deliverit.repositories.ParcelRepository;
import com.team9.deliverit.services.CustomerService;
import com.team9.deliverit.services.ParcelService;
import com.team9.deliverit.services.ShipmentService;
import com.team9.deliverit.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParcelModelMapper {

    private final ShipmentService shipmentService;
    private final CustomerService customerService;
    private final ParcelService parcelService;
    private final WarehouseService warehouseService;

    @Autowired
    public ParcelModelMapper(ShipmentService shipmentService, CustomerService customerService, ParcelService parcelService, WarehouseService warehouseService) {
        this.shipmentService = shipmentService;
        this.customerService = customerService;
        this.parcelService = parcelService;
        this.warehouseService = warehouseService;
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
        Warehouse warehouse = warehouseService.getById(parcelDto.getWarehouseId());
        Shipment shipment = shipmentService.getById(parcelDto.getShipmentId());
        Customer customer = customerService.getById(parcelDto.getCustomerId());

        parcel.setWeight(parcelDto.getWeight());
        parcel.setCategory(parcelDto.getCategory());
        parcel.setPickUpOption(parcelDto.getPickUpOption());
        parcel.setCustomer(customer);
        parcel.setShipment(shipment);
        parcel.setWarehouse(warehouse);

    }

}