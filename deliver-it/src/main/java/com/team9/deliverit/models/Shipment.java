package com.team9.deliverit.models;

import com.team9.deliverit.models.enums.Status;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.sql.Date;

@Entity
@Table(name = "shipments")
public class Shipment {

    @Positive(message = "ID should be a positive number!")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private int id;

    private Date departureDate;

    private Date arrivalDate;

    @Enumerated(EnumType.STRING)
    private Status status;

   // private Warehouse originWarehouse;

    //private Warehouse destinationWarehouse;

    public Shipment() {
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

/*    public Warehouse getOriginWarehouse() {
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
    }*/
}
