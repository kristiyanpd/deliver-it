package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ShipmentRepositoryImpl {

    private final List<Shipment> shipments;

    public ShipmentRepositoryImpl() {
        this.shipments = new ArrayList<>();
    }

    public List<Shipment> getAllShipments() {
        return shipments;
    }

    public Shipment getShipmentById(int id) {
        return shipments.stream()
                .filter(shipment -> shipment.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Shipment", id));
    }

    public void createShipment(Shipment shipment) {
        shipments.add(shipment);
    }

    public Shipment updateShipment(Shipment shipment) {
        Shipment shipmentToUpdate = getShipmentById(shipment.getId());

        shipmentToUpdate.setArrivalDate(shipment.getArrivalDate());
        shipmentToUpdate.setDepartureDate(shipment.getDepartureDate());
        shipmentToUpdate.setDestinationWarehouse(shipment.getDestinationWarehouse());
        shipmentToUpdate.setOriginWarehouse(shipment.getOriginWarehouse());
        shipmentToUpdate.setStatus(shipment.getStatus());

        return shipmentToUpdate;
    }

    public void deleteShipment(int id) {
        Shipment shipmentToDelete = getShipmentById(id);
        shipments.remove(shipmentToDelete);
    }

    public List<Shipment> filterByDestinationWarehouse(Warehouse warehouse) {
        return shipments.stream()
                .filter(shipment -> shipment.getDestinationWarehouse() == warehouse)
                .collect(Collectors.toList());
    }


}
