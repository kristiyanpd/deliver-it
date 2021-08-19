package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends BaseRepository<Shipment> {

    int countShipmentsOnTheWay();

    Shipment nextShipmentToArrive(int warehouseId);

    boolean isEmpty(int shipmentId);

    List<Parcel> getParcels(int shipmentId);

    void delete(int id);

    List<Shipment> filter(Optional<Integer> warehouseId, Optional<Integer> customerId);
}
