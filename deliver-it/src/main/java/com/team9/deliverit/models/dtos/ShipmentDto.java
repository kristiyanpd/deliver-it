package com.team9.deliverit.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Date;

public class ShipmentDto {

    private Date departureDate;

    private Date arrivalDate;

    private String status;

    @NotNull
    private boolean isFull;

    @Positive(message = "Origin Warehouse ID must be positive!")
    private int originWarehouseId;

    @Positive(message = "Destination Warehouse ID must be positive!")
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOriginWarehouseId() {
        return originWarehouseId;
    }

    public void setOriginWarehouseId(int originWarehouseId) {
        this.originWarehouseId = originWarehouseId;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean isFull) {
        this.isFull = isFull;
    }

    public int getDestinationWarehouseId() {
        return destinationWarehouseId;
    }

    public void setDestinationWarehouseId(int destinationWarehouseId) {
        this.destinationWarehouseId = destinationWarehouseId;
    }

}
