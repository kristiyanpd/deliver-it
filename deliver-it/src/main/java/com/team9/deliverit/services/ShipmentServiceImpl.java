package com.team9.deliverit.services;

import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
import com.team9.deliverit.services.contracts.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<Shipment> filter(Optional<Integer> warehouseId, Optional<Integer> customerId) {
        return repository.filter(warehouseId, customerId);
    }

}
