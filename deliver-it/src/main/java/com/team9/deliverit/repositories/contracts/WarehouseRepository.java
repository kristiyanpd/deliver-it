package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Warehouse;

import java.util.List;

public interface WarehouseRepository {
    List<Warehouse> getAll();

    Warehouse getById(int id);

    void create(Warehouse warehouse);

    void update(Warehouse warehouse);

    void delete(int id);

    Warehouse getByAddressId(int id);
}