package com.team9.deliverit.services;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.Category;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ParcelService {
    List<Parcel> getAll();

    Parcel getById(int id);

    void create(Parcel parcel);

    void update(Parcel parcel);

    void delete(int id);

    List<Parcel> filter(Optional<Double> weight, Optional<Integer> customerId, Optional<Integer> warehouseId, Optional<Category> category);

    List<Parcel> sortByWeight();

    List<Parcel> sortByArrivalDate();

    List<Parcel> sortByWeightAndArrivalDate();

    List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate);
}
