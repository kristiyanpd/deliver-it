package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.EnumAlreadySameException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
import com.team9.deliverit.services.contracts.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

@Service
public class ParcelServiceImpl implements ParcelService {

    public static final String UNAUTHORIZED_NOT_OWNER = "You are not the owner of this parcel!";
    public static final String INVALID_SHIPMENT_FULL = "Parcel cannot be added to a shipment that is full!";

    private final ParcelRepository repository;
    private final ShipmentRepository shipmentRepository;

    @Autowired
    public ParcelServiceImpl(ParcelRepository repository, ShipmentRepository shipmentRepository) {
        this.repository = repository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public List<Parcel> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "view all", "parcels"));
        }
        return repository.getAll();
    }

    @Override
    public Parcel getById(User user, int id) {
        if (!user.isEmployee() && repository.getById(id).getUser().getId() != user.getId()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_NOT_OWNER);
        }
        return repository.getById(id);
    }

    @Override
    public void create(User user, Parcel parcel) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "create", "parcels"));
        }
        if (parcel.getShipment().isFull()) {
            throw new IllegalArgumentException(INVALID_SHIPMENT_FULL);
        }
        repository.create(parcel);
    }

    @Override
    public void update(User user, Parcel parcel) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "update", "parcels"));
        }
        Parcel oldParcel = getById(user, parcel.getId());
        if (parcel.getShipment().isFull() && oldParcel.getShipment().getId() != parcel.getShipment().getId()) {
            throw new IllegalArgumentException(INVALID_SHIPMENT_FULL);
        }
        repository.update(parcel);
    }

    @Override
    public void delete(User user, int id) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "delete", "parcels"));
        }
        repository.delete(id);
    }

    @Override
    public List<Parcel> filter(User user, Optional<Double> weight, Optional<Integer> warehouseId, Optional<Category> category, Optional<Status> status, Optional<Integer> userId) {
        if (!user.isEmployee()) {
            return repository.filter(weight, warehouseId, category, status, Optional.of(user.getId()));
        }
        return repository.filter(weight, warehouseId, category, status, userId);
    }

    @Override
    public List<Parcel> sort(User user, Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId) {
        if (!user.isEmployee()) {
            return repository.sort(weight, arrivalDate, Optional.of(user.getId()));
        }
        return repository.sort(weight, arrivalDate, userId);
    }

    @Override
    public List<Parcel> getAllUserParcels(User user) {
        return repository.getAllUserParcels(user.getId());
    }

    @Override
    public List<Parcel> incomingParcels(User user) {
        return repository.incomingParcels(user.getId());
    }

    @Override
    public List<Parcel> pastParcels(User user) {
        return repository.pastParcels(user.getId());
    }

    @Override
    public void updatePickUpOption(User user, Parcel parcel, String pickUpOption) {
        if (parcel.getUser().getId() != user.getId()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_NOT_OWNER);
        }
        PickUpOption pick = PickUpOption.getEnum(pickUpOption);
        if (parcel.getShipment().getStatus() == Status.COMPLETED) {
            throw new EnumAlreadySameException(String.format("Parcel with ID %s is already %s!", parcel.getId(), parcel.getShipment().getStatus().toString()));
        }
        parcel.setPickUpOption(pick);
        repository.update(parcel);
    }

    @Override
    public void updateShipment(User user, Parcel parcel, int shipmentId) {
        Shipment shipment = shipmentRepository.getById(shipmentId);
        parcel.setShipment(shipment);
        update(user, parcel);
    }
}
