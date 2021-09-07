package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
import com.team9.deliverit.services.contracts.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private static final String SHIPMENT_INVALID_WAREHOUSE = "Shipments can't have same origin and destination warehouse!";

    private final ShipmentRepository repository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.repository = shipmentRepository;
    }


    @Override
    public List<Shipment> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view all", "shipments"));
        }
        return repository.getAll();
    }

    @Override
    public Shipment getById(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view", "shipments"));
        }
        return repository.getById(id);
    }

    @Override
    public void create(User user, Shipment shipment) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "create", "shipments"));
        }
        if (shipment.getOriginWarehouse().getId() == shipment.getDestinationWarehouse().getId()) {
            throw new IllegalArgumentException(SHIPMENT_INVALID_WAREHOUSE);
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
            throw new IllegalArgumentException(SHIPMENT_INVALID_WAREHOUSE);
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
        } else if (getParcels(id, user).size() != 0) {
            throw new IllegalArgumentException("You can' t delete a shipment that has parcels!");
        }
        repository.delete(id);
    }

    @Override
    public List<Shipment> filter(User user, Optional<Integer> warehouseId, Optional<Integer> customerId) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "filter", "shipments"));
        }
        return repository.filter(warehouseId, customerId);
    }

    @Override
    public int countShipmentsOnTheWay(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "count incoming", "shipments!"));
        }
        return repository.countShipmentsOnTheWay();
    }

    @Override
    public List<Parcel> getParcels(int shipmentId, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "get all", "parcels from a shipment!"));
        }
        return repository.getParcels(shipmentId);
    }

    @Override
    public Shipment nextShipmentToArrive(int warehouseId, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view the next arriving", "shipment"));
        }
        return repository.nextShipmentToArrive(warehouseId);
    }

}
