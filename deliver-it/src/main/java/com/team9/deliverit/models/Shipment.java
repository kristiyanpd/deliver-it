package com.team9.deliverit.models;

import javax.validation.constraints.Positive;
import java.sql.Date;

public class Shipment {

    @Positive(message = "ID should be a positive number!")
    private int id;

    private Date departureDate;

    private Date arrivalDate;

    private Status status;

    private Warehouse originWarehouse;

    private Warehouse destinationWarehouse;

    public Shipment(int id, Date departureDate, Date arrivalDate, Status status, Warehouse originWarehouse, Warehouse destinationWarehouse) {
        setId(id);
        setDepartureDate(departureDate);
        setArrivalDate(arrivalDate);
        setStatus(status);
        setOriginWarehouse(originWarehouse);
        setDestinationWarehouse(destinationWarehouse);
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

    public Warehouse getOriginWarehouse() {
        return originWarehouse;
    }

    public void setOriginWarehouse(Warehouse originWarehouse) {
        this.originWarehouse = originWarehouse;
    }

    public Warehouse getDestinationWarehouse() {
        return destinationWarehouse;
    }

    public void setDestinationWarehouse(Warehouse destinationWarehouse) {
        this.destinationWarehouse = destinationWarehouse;
    }
}
