package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
import com.team9.deliverit.services.contracts.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParcelServiceImpl implements ParcelService {

    public static final String UNAUTHORIZED_NOT_OWNER = "You are not the owner of this parcel!";
    private final ParcelRepository repository;

    @Autowired
    public ParcelServiceImpl(ParcelRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Parcel> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view all parcels!");
        }
        return repository.getAll();
    }

    @Override
    public Parcel getById(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view a given parcel!");
        }
        return repository.getById(id);
    }

    @Override
    public void create(Parcel parcel, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can create a parcel!");
        }
        repository.create(parcel);
    }

    @Override
    public void update(Parcel parcel, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can update parcels!");
        }
        repository.update(parcel);
    }

    @Override
    public void delete(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can delete parcels!");
        }
        repository.delete(id);
    }

    @Override
    public List<Parcel> filter(Optional<Double> weight, Optional<Integer> warehouseId, Optional<Category> category, Optional<Status> status, Optional<Integer> userId, User user) {
        if (!user.isEmployee()) {
            return repository.filter(weight, warehouseId, category, status, Optional.of(user.getId()));
        }
        return repository.filter(weight, warehouseId, category, status, userId);
    }

    @Override
    public List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId, User user) {
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
    public String getStatusOfParcel(User user, int parcelId) {
        if (repository.getById(parcelId).getUser().getId() != user.getId()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_NOT_OWNER);
        }
        return repository.getStatusOfParcel(parcelId);
    }

    @Override
    public Parcel updatePickUpOption(User user, int parcelId, String pickUpOption) {
        if (repository.getById(parcelId).getUser().getId() != user.getId()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_NOT_OWNER);
        }
        PickUpOption pick = PickUpOption.getEnum(pickUpOption);
        return repository.updatePickUpOption(parcelId, pick);
    }
}
