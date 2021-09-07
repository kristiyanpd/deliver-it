package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.Status;

import java.util.List;
import java.util.Optional;

public interface ParcelService {
    List<Parcel> getAll(User user);

    Parcel getById(User user, int id);

    void create(User user, Parcel parcel);

    void update(User user, Parcel parcel);

    void delete(User user, int id);

    List<Parcel> filter(User user, Optional<Double> weight, Optional<Integer> warehouseId, Optional<Category> category, Optional<Status> status, Optional<Integer> userId);

    List<Parcel> sort(User user, Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId);

    List<Parcel> getAllUserParcels(User user);

    List<Parcel> incomingParcels(User user);

    List<Parcel> pastParcels(User user);

    void updatePickUpOption(User user, Parcel parcel, String pickUpOption);

    void updateShipment(User user, Parcel parcel, int shipmentId);

    int parcelsCount(User user);

}
