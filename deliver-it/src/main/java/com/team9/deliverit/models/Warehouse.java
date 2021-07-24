package com.team9.deliverit.models;

import javax.validation.constraints.Positive;

public class Warehouse {

    @Positive(message = "ID should be a positive number!")
    private int id;

    Address address;

    public Warehouse(int id, Address address) {
        setId(id);
        setAddress(address);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
