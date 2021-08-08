package com.team9.deliverit.models.dtos;

import com.team9.deliverit.models.enums.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Date;

public class ShipmentDisplayDto {

    private int id;

    private Date departureDate;

    private Date arrivalDate;

    private Status status;

    private boolean isFull;

    private AddressDisplayDto originWarehouseAddress;

    private AddressDisplayDto destinationWarehouseAddress;

    public ShipmentDisplayDto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public AddressDisplayDto getOriginWarehouseAddress() {
        return originWarehouseAddress;
    }

    public void setOriginWarehouseAddress(AddressDisplayDto originWarehouseAddress) {
        this.originWarehouseAddress = originWarehouseAddress;
    }

    public AddressDisplayDto getDestinationWarehouseAddress() {
        return destinationWarehouseAddress;
    }

    public void setDestinationWarehouseAddress(AddressDisplayDto destinationWarehouseAddress) {
        this.destinationWarehouseAddress = destinationWarehouseAddress;
    }
}
