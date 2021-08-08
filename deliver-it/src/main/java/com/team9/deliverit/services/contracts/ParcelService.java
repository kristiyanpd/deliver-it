package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDisplayDto;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.Status;

import java.util.List;
import java.util.Optional;

public interface ParcelService {
    List<ParcelDisplayDto> getAll(User user);

    ParcelDisplayDto getById(int id, User user);

    void create(Parcel parcel, User user);

    void update(Parcel parcel, User user);

    void delete(int id, User user);

    List<ParcelDisplayDto> filter(Optional<Double> weight, Optional<Integer> warehouseId, Optional<Category> category, Optional<Status> status, Optional<Integer> userId, User user);

    List<ParcelDisplayDto> sort(Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId, User user);

    List<ParcelDisplayDto> getAllUserParcels(User user);

    String getStatusOfParcel(User user, int parcelId);

    ParcelDisplayDto updatePickUpOption(User user, int parcelId, String pickUpOption);
}
