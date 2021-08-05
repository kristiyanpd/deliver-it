package com.team9.deliverit.services;

import com.team9.deliverit.models.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentService {
    List<Shipment> getAll();

    Shipment getById(int id);

    void create(Shipment shipment);

    void update(Shipment shipment);

    void delete(int id);

    List<Shipment> filter(Optional<Integer> warehouseId, Optional<Integer> customerId);

}
