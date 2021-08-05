package com.team9.deliverit.services;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
import com.team9.deliverit.services.contracts.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParcelServiceImpl implements ParcelService {

    private ParcelRepository repository;

    @Autowired
    public ParcelServiceImpl(ParcelRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Parcel> getAll() {
        return repository.getAll();
    }

    @Override
    public Parcel getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Parcel parcel) {
      repository.create(parcel);
    }

    @Override
    public void update(Parcel parcel) {
     repository.update(parcel);
    }

    @Override
    public void delete(int id) {
       repository.delete(id);
    }

    @Override
    public List<Parcel> filter(Optional<Double> weight, Optional<Integer> customerId, Optional<Integer> warehouseId, Optional<Category> category) {
        return repository.filter(weight,customerId,warehouseId,category);
    }

    @Override
    public List<Parcel> sortByWeight() {
        return repository.sortByWeight();
    }

    @Override
    public List<Parcel> sortByArrivalDate() {
        return repository.sortByArrivalDate();
    }

    @Override
    public List<Parcel> sortByWeightAndArrivalDate() {
        return repository.sortByWeightAndArrivalDate();
    }

    @Override
    public List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate) {
        return repository.sort(weight,arrivalDate);
    }
}
