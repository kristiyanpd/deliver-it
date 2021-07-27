package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Warehouse;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseRepositoryImpl {

        private final List<Warehouse> warehouses;

        public WarehouseRepositoryImpl() {
            this.warehouses = new ArrayList<>();
        }

        public List<Warehouse> getAllWarehouses() {
            return warehouses;
        }

        public Warehouse getWarehouseById(int id) {
            return warehouses.stream()
                    .filter(warehouse -> warehouse.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Warehouse", id));
        }
}
