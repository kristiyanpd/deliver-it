package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ShipmentDisplayDto;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.mappers.ShipmentModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository repository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.repository = shipmentRepository;
    }


    @Override
    public List<ShipmentDisplayDto> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view all shipments!");
        }
        return repository.getAll()
                .stream().map(ShipmentModelMapper::toShipmentDto).collect(Collectors.toList());
    }

    @Override
    public ShipmentDisplayDto getById(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view shipments by ID!");
        }
        return ShipmentModelMapper.toShipmentDto(repository.getById(id));
    }

    @Override
    public void create(User user, Shipment shipment) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can create shipments!");
        }
        if (shipment.getOriginWarehouse().getId() == shipment.getDestinationWarehouse().getId()) {
            throw new IllegalArgumentException("Shipments can't have same origin and destination warehouse!");
        }
        repository.create(shipment);
    }

    @Override
    public void update(User user, Shipment shipment) {
        Shipment shipmentToEdit = repository.getById(shipment.getId());
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can modify shipments!");
        }
        if (shipment.getOriginWarehouse().getId() == shipment.getDestinationWarehouse().getId()) {
            throw new IllegalArgumentException("Shipments can't have same origin and destination warehouse!");
        }
        if (repository.isEmpty(shipment.getId()) && shipmentToEdit.getStatus() != shipment.getStatus()) {
            throw new IllegalArgumentException("You can only modify the status of a shipment that has parcels!");
        }
        repository.update(shipment);
    }

    @Override
    public void delete(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can delete shipments!");
        }
        repository.delete(id);
    }

    @Override
    public List<ShipmentDisplayDto> filter(User user, Optional<Integer> warehouseId, Optional<Integer> customerId) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can filter shipments!");
        }
        return repository.filter(warehouseId, customerId)
                .stream().map(ShipmentModelMapper::toShipmentDto).collect(Collectors.toList());
    }

    @Override
    public int countShipmentsOnTheWay(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("You are not authorised for this operation");
        }
        return repository.countShipmentsOnTheWay();
    }

    @Override
    public ShipmentDisplayDto nextShipmentToArrive(int warehouseId, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("You are not authorised for this operation");
        }
        return ShipmentModelMapper.toShipmentDto(repository.nextShipmentToArrive(warehouseId));
    }

}
