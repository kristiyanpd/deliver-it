package com.team9.deliverit.services.mappers;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.WarehouseDto;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import com.team9.deliverit.services.contracts.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarehouseModelMapper {

    private final WarehouseService warehouseService;
    private final AddressRepository addressRepository;

    @Autowired
    public WarehouseModelMapper(WarehouseService warehouseService, AddressRepository addressRepository) {
        this.warehouseService = warehouseService;
        this.addressRepository = addressRepository;
    }

    public Warehouse fromDto(WarehouseDto warehouseDto) {
        Warehouse warehouse = new Warehouse();
        dtoToObject(warehouseDto, warehouse);
        return warehouse;
    }

    public Warehouse fromDto(WarehouseDto cityDto, int id) {
        Warehouse city = warehouseService.getById(id);
        dtoToObject(cityDto, city);
        return city;
    }

    private void dtoToObject(WarehouseDto warehouseDto, Warehouse warehouse) {
        Address address = addressRepository.getById(warehouseDto.getAddressId());
        warehouse.setAddress(address);
    }

}