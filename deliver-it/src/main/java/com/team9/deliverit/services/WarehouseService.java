package com.team9.deliverit.services;

import com.team9.deliverit.models.Warehouse;

import java.util.List;

public interface WarehouseService {
    List<Warehouse> getAll();

    Warehouse getById(int id);

    void create(Warehouse warehouse);

    void update(Warehouse warehouse);

    void delete(int id);
}
