package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.Status;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends BaseRepository<Parcel> {

    List<Parcel> getAllUserParcels(int userId);

    List<Parcel> incomingParcels(int userId);

    List<Parcel> pastParcels(int userId);

    List<Parcel> filter(Optional<Double> weight,
                        Optional<Integer> warehouseId,
                        Optional<Category> category,
                        Optional<Status> status,
                        Optional<Integer> userId);

    List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId);

}
