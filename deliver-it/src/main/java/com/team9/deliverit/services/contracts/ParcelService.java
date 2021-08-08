package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.Status;

import java.util.List;
import java.util.Optional;

public interface ParcelService {
    List<Parcel> getAll(User user);

    Parcel getById(int id, User user);

    void create(Parcel parcel, User user);

    void update(Parcel parcel, User user);

    void delete(int id, User user);

    List<Parcel> filter(Optional<Double> weight, Optional<Integer> warehouseId, Optional<Category> category, Optional<Status> status, Optional<Integer> userId, User user);

    List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId, User user);

    List<Parcel> getAllUserParcels(User user);

    String getStatusOfParcel(User user, int parcelId);

    Parcel updatePickUpOption(User user, int parcelId, String pickUpOption);
}
