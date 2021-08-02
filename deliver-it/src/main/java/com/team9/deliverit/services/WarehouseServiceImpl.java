package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.repository = warehouseRepository;
    }

    @Override
    public List<Warehouse> getAll() {
        return repository.getAll();
    }

    @Override
    public Warehouse getById(int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Warehouse warehouse) {
        boolean duplicateExists = true;
        try {
            repository.getByAddressId(warehouse.getAddress().getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new DuplicateEntityException("Warehouse", "address_id", String.valueOf(warehouse.getAddress().getId()));
        }
        repository.create(warehouse);
    }

    @Override
    public void update(Warehouse warehouse) {
        boolean duplicateExists = true;
        try {
            Warehouse existingWarehouse = repository.getByAddressId(warehouse.getAddress().getId());
            if (existingWarehouse.getId() == warehouse.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Warehouse", "address_id", String.valueOf(warehouse.getId()));
        }

        repository.update(warehouse);
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
    }
}
