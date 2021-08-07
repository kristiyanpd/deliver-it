package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.enums.Category;

import java.util.List;
import java.util.Optional;

public interface ParcelService {
    List<Parcel> getAll();

    Parcel getById(int id);

    void create(Parcel parcel);

    void update(Parcel parcel);

    void delete(int id);

    List<Parcel> filter(Optional<Double> weight, Optional<Integer> userId, Optional<Integer> warehouseId, Optional<Category> category);

    List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate);

    List<Parcel> getAllUserParcels(User user);

    String getStatusOfParcel(User user, int parcelId);

    Parcel updatePickUpOption(User user, int parcelId, String pickUpOption);
}
