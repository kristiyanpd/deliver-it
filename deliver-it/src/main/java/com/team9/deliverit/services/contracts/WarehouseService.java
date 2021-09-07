package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.User;
import com.team9.deliverit.models.Warehouse;

import java.util.List;

public interface WarehouseService {
    List<Warehouse> getAll();

    Warehouse getById(User user, int id);

    void create(Warehouse warehouse, User user);

    void update(Warehouse warehouse, User user);

    void delete(int id, User user);

    int warehousesCount();
}
