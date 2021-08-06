package com.team9.deliverit.models.dtos;

import javax.validation.constraints.Positive;

public class WarehouseDto {

    @Positive(message = "Address ID should be positive!")
    private int addressId;

    public WarehouseDto() {
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}