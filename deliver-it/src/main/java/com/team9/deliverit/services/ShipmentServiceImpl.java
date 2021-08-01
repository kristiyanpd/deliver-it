package com.team9.deliverit.services;

import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.repositories.ShipmentRepository;
import com.team9.deliverit.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository repository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.repository = shipmentRepository;
    }


    @Override
    public List<Shipment> getAll() {
        return repository.getAll();
    }

    @Override
    public Shipment getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Shipment shipment) {
       repository.create(shipment);
    }

    @Override
    public void update(Shipment shipment) {
      repository.update(shipment);
    }

    @Override
    public void delete(int id) {
      repository.delete(id);
    }

    @Override
    public List<Shipment> filterByDestinationWarehouse(int warehouseId) {
        return repository.filterByDestinationWarehouse(warehouseId);
    }

    @Override
    public List<Shipment> filterByCustomer(int customerId) {
        return repository.filterByCustomer(customerId);
    }
}
