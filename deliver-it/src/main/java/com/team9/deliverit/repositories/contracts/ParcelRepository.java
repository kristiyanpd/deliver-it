package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository {
    List<Parcel> getAll();

    Parcel getById(int id);

    void create(Parcel parcel);

    void update(Parcel parcel);

    void delete(int id);

    List<Parcel> getAllUserParcels(int userId);

    Parcel updatePickUpOption(int parcelId, PickUpOption pickUpOption);

    String getStatusOfParcel(int parcelId);

    List<Parcel> filter(Optional<Double> weight, Optional<Integer> userId,
                        Optional<Integer> warehouseId, Optional<Category> category);

    List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate);
}
