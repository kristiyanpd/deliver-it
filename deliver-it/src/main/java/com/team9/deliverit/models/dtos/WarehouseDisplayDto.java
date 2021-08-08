package com.team9.deliverit.models.dtos;

public class WarehouseDisplayDto {


    private int id;

    private AddressDisplayDto address;

    public WarehouseDisplayDto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AddressDisplayDto getAddress() {
        return address;
    }

    public void setAddress(AddressDisplayDto address) {
        this.address = address;
    }
}
