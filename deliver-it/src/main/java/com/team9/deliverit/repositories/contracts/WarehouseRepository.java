package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Warehouse;

public interface WarehouseRepository extends BaseRepository<Warehouse> {

    boolean isDuplicate(int addressId);
}