package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.WarehouseDto;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import com.team9.deliverit.repositories.contracts.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarehouseModelMapper {

    private final WarehouseRepository warehouseRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public WarehouseModelMapper(WarehouseRepository warehouseRepository, AddressRepository addressRepository) {
        this.warehouseRepository = warehouseRepository;
        this.addressRepository = addressRepository;
    }

    public Warehouse fromDto(WarehouseDto warehouseDto) {
        Warehouse warehouse = new Warehouse();
        dtoToObject(warehouseDto, warehouse);
        return warehouse;
    }

    public Warehouse fromDto(WarehouseDto cityDto, int id) {
        Warehouse city = warehouseRepository.getById(id);
        dtoToObject(cityDto, city);
        return city;
    }

    private void dtoToObject(WarehouseDto warehouseDto, Warehouse warehouse) {
        Address address = addressRepository.getById(warehouseDto.getAddressId());
        warehouse.setAddress(address);
    }

}