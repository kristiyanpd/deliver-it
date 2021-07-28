package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Category;
import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ParcelRepositoryImpl {

    private final List<Parcel> parcels;

    public ParcelRepositoryImpl() {
        parcels = new ArrayList<>();
    }

    public List<Parcel> getAll(){
        return parcels;
    }

    public Parcel getById(int id){
        return parcels
                .stream()
                .filter(parcel -> parcel.getId()==id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Parcel", id));
    }

    public void create(Parcel parcel){
        parcels.add(parcel);
    }

    public Parcel update(Parcel parcel){
        Parcel parcelToUpdate = getById(parcel.getId());

        parcelToUpdate.setCategory(parcel.getCategory());
        parcelToUpdate.setCustomer(parcel.getCustomer());
        parcelToUpdate.setShipment(parcel.getShipment());
        parcelToUpdate.setWarehouse(parcel.getWarehouse());
        parcelToUpdate.setPickUpFromWarehouse(parcel.isPickUpFromWarehouse());
        parcelToUpdate.setWeight(parcel.getWeight());
        parcelToUpdate.setCustomer(parcel.getCustomer());

        return parcel;
    }

    public void delete(int id){
        Parcel parcelToDelete = getById(id);
        parcels.remove(parcelToDelete);
    }

    public List<Parcel> filterByCustomer(Customer customer){
        return parcels
                .stream()
                .filter(parcel -> parcel.getCustomer() == customer)
                .collect(Collectors.toList());

    }

    public List<Parcel> filterByWarehouse(Warehouse warehouse){
        return parcels
                .stream()
                .filter(parcel -> parcel.getWarehouse()==warehouse)
                .collect(Collectors.toList());
    }

    public List<Parcel> filterByCategory(Category category){
        return parcels
                .stream()
                .filter(parcel -> parcel.getCategory()==category)
                .collect(Collectors.toList());
    }


}
