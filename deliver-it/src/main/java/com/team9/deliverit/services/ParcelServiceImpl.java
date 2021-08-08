package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDisplayDto;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
import com.team9.deliverit.services.contracts.ParcelService;
import com.team9.deliverit.services.mappers.ParcelModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParcelServiceImpl implements ParcelService {

    public static final String UNAUTHORIZED_NOT_OWNER = "You are not the owner of this parcel!";
    private final ParcelRepository repository;

    @Autowired
    public ParcelServiceImpl(ParcelRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ParcelDisplayDto> getAll(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view all parcels!");
        }
        return repository.getAll().stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @Override
    public ParcelDisplayDto getById(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can view parcels by ID!");
        }
        return ParcelModelMapper.toParcelDto(repository.getById(id));
    }

    @Override
    public void create(Parcel parcel, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can create parcels!");
        }
        repository.create(parcel);
    }

    @Override
    public void update(Parcel parcel, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Only employees can modify parcels!");
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
    public List<ParcelDisplayDto> filter(Optional<Double> weight, Optional<Integer> warehouseId, Optional<Category> category, Optional<Status> status, Optional<Integer> userId, User user) {
        if (!user.isEmployee()) {
            return repository
                    .filter(weight, warehouseId, category, status, Optional.of(user.getId()))
                    .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
        }
        return repository.filter(weight, warehouseId, category, status, userId)
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @Override
    public List<ParcelDisplayDto> sort(Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId, User user) {
        if (!user.isEmployee()) {
            return repository.sort(weight, arrivalDate, Optional.of(user.getId()))
                    .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
        }
        return repository.sort(weight, arrivalDate, userId)
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @Override
    public List<ParcelDisplayDto> getAllUserParcels(User user) {
        return repository.getAllUserParcels(user.getId())
                .stream().map(ParcelModelMapper::toParcelDto).collect(Collectors.toList());
    }

    @Override
    public String getStatusOfParcel(User user, int parcelId) {
        if (repository.getById(parcelId).getUser().getId() != user.getId()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_NOT_OWNER);
        }
        return repository.getStatusOfParcel(parcelId);
    }

    @Override
    public ParcelDisplayDto updatePickUpOption(User user, int parcelId, String pickUpOption) {
        if (repository.getById(parcelId).getUser().getId() != user.getId()) {
            throw new UnauthorizedOperationException(UNAUTHORIZED_NOT_OWNER);
        }
        PickUpOption pick = PickUpOption.getEnum(pickUpOption);
        return ParcelModelMapper.toParcelDto(repository.updatePickUpOption(parcelId, pick));
    }
}
