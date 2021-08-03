package com.team9.deliverit.models.dtos;

import com.team9.deliverit.models.enums.Status;

import javax.validation.constraints.Positive;
import java.sql.Date;

public class ShipmentDto {

    private Date departureDate;

    private Date arrivalDate;

    private Status status;

    @Positive(message = "Origin Warehouse ID should be positive!")
    private int originWarehouseId;

    @Positive(message = "Destination Warehouse ID should be positive!")
    private int destinationWarehouseId;

    public ShipmentDto() {

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

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public int getOriginWarehouseId() {
        return originWarehouseId;
    }

    public void setOriginWarehouseId(int originWarehouseId) {
        this.originWarehouseId = originWarehouseId;
    }

    public int getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public void setDestinationWarehouseId(int destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

}
