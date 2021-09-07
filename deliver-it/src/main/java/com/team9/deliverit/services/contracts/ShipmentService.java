package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface ShipmentService {
    List<Shipment> getAll(User user);

    Shipment getById(User user, int id);

    void create(User user, Shipment shipment);

    void update(User user, Shipment shipment);

    void delete(User user, int id);

    List<Shipment> filter(User user, Optional<Integer> warehouseId, Optional<Integer> customerId);

    List<Parcel> getParcels(int shipmentId, User user);

    int countShipmentsOnTheWay(User user);

    Shipment nextShipmentToArrive(int warehouseId, User user);

    int shipmentsCount();

}
