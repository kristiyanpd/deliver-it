package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.ParcelDisplayDto;
import com.team9.deliverit.models.dtos.ShipmentDisplayDto;
import com.team9.deliverit.models.dtos.ShipmentDto;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
import com.team9.deliverit.repositories.contracts.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentModelMapper {

    private final ShipmentRepository shipmentRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public ShipmentModelMapper(ShipmentRepository shipmentRepository, WarehouseRepository warehouseRepository) {
        this.shipmentRepository = shipmentRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public static ShipmentDisplayDto toShipmentDto(Shipment shipment){
        ShipmentDisplayDto shipmentDisplayDto = new ShipmentDisplayDto();

        shipmentDisplayDto.setId(shipment.getId());
        shipmentDisplayDto.setArrivalDate(shipment.getArrivalDate());
        shipmentDisplayDto.setDepartureDate(shipment.getDepartureDate());
        shipmentDisplayDto.setStatus(shipment.getStatus());
        shipmentDisplayDto.setFull(shipment.isFull());

        shipmentDisplayDto.setOriginWarehouseAddress(AddressModelMapper.toAddressDto(shipment.getOriginWarehouse().getAddress()));
        shipmentDisplayDto.setDestinationWarehouseAddress(AddressModelMapper.toAddressDto(shipment.getDestinationWarehouse().getAddress()));

        return shipmentDisplayDto;
    }

    public Shipment fromDto(ShipmentDto shipmentDto) {
        Shipment shipment = new Shipment();
        dtoToObject(shipmentDto, shipment);
        return shipment;
    }

    public Shipment fromDto(ShipmentDto shipmentDto, int id) {
        Shipment shipment = shipmentRepository.getById(id);
        dtoToObject(shipmentDto, shipment);
        return shipment;
    }

    private void dtoToObject(ShipmentDto shipmentDto, Shipment shipment) {
        Warehouse originWarehouse = warehouseRepository.getById(shipmentDto.getOriginWarehouseId());
        Warehouse destinationWarehouse = warehouseRepository.getById(shipmentDto.getDestinationWarehouseId());
        shipment.setDepartureDate(shipmentDto.getDepartureDate());
        shipment.setArrivalDate(shipmentDto.getArrivalDate());
        shipment.setStatus(Status.getEnum(shipmentDto.getStatus()));
        shipment.setOriginWarehouse(originWarehouse);
        shipment.setDestinationWarehouse(destinationWarehouse);
        shipment.setFull(shipmentDto.isFull());
    }

}