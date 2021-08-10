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

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

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
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view all", "shipments"));
        }
        return repository.getAll()
                .stream().map(ShipmentModelMapper::toShipmentDto).collect(Collectors.toList());
    }

    @Override
    public ShipmentDisplayDto getById(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view", "shipments"));
        }
        return ShipmentModelMapper.toShipmentDto(repository.getById(id));
    }

    @Override
    public void create(User user, Shipment shipment) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "create", "shipments"));
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
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "modify", "shipments"));
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
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "delete", "shipments"));
        }
        repository.delete(id);
    }

    @Override
    public List<ShipmentDisplayDto> filter(User user, Optional<Integer> warehouseId, Optional<Integer> customerId) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "filter", "shipments"));
        }
        return repository.filter(warehouseId, customerId)
                .stream().map(ShipmentModelMapper::toShipmentDto).collect(Collectors.toList());
    }

    @Override
    public int countShipmentsOnTheWay(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "count incoming", "shipments!"));
        }
        return repository.countShipmentsOnTheWay();
    }

    @Override
    public ShipmentDisplayDto nextShipmentToArrive(int warehouseId, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view the next arriving", "shipment"));
        }
        return ShipmentModelMapper.toShipmentDto(repository.nextShipmentToArrive(warehouseId));
    }

}
