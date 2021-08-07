package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.ShipmentDto;
import com.team9.deliverit.repositories.contracts.WarehouseRepository;
import com.team9.deliverit.services.contracts.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentModelMapper {

    private final ShipmentService shipmentService;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public ShipmentModelMapper(ShipmentService shipmentService, WarehouseRepository warehouseRepository) {
        this.shipmentService = shipmentService;
        this.warehouseRepository = warehouseRepository;
    }

    public Shipment fromDto(ShipmentDto shipmentDto) {
        Shipment shipment = new Shipment();
        dtoToObject(shipmentDto, shipment);
        return shipment;
    }

    public Shipment fromDto(ShipmentDto shipmentDto, int id) {
        Shipment shipment = shipmentService.getById(id);
        dtoToObject(shipmentDto, shipment);
        return shipment;
    }

    private void dtoToObject(ShipmentDto shipmentDto, Shipment shipment) {
        Warehouse originWarehouse = warehouseRepository.getById(shipmentDto.getOriginWarehouseId());
        Warehouse destinationWarehouse = warehouseRepository.getById(shipmentDto.getDestinationWarehouseId());
        shipment.setDepartureDate(shipmentDto.getDepartureDate());
        shipment.setArrivalDate(shipmentDto.getArrivalDate());
        shipment.setStatus(shipmentDto.getStatus());
        shipment.setOriginWarehouse(originWarehouse);
        shipment.setDestinationWarehouse(destinationWarehouse);
        shipment.setHasFreeSpace(true);
    }

}