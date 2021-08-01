package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Shipment;

import java.util.List;

public interface ShipmentRepository {
    List<Shipment> getAll();

    Shipment getById(int id);

    void create(Shipment shipment);

    void update(Shipment shipment);

    void delete(int id);

    List<Shipment> filterByDestinationWarehouse(int warehouseId);

    List<Shipment> filterByCustomer(int customerId);
}
