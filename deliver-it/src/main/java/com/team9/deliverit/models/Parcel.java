package com.team9.deliverit.models;

import javax.validation.constraints.Positive;

public class Parcel {

    @Positive(message = "Parcel id must be positive")
    private int id;

    @Positive(message = "Weight should be positive")
    private double weight;

    private boolean pickUpFromWarehouse;

    private Warehouse warehouse;

    private Customer customer;

    private  Shipment shipment;

    private Category category;

    public Parcel(int id, double weight, boolean pickUpFromWarehouse, Warehouse warehouse, Customer customer, Shipment shipment, Category category) {
        setId(id);
        setWeight(weight);
        setPickUpFromWarehouse(pickUpFromWarehouse);
        setWarehouse(warehouse);
        setCustomer(customer);
        setShipment(shipment);
        setCategory(category);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isPickUpFromWarehouse() {
        return pickUpFromWarehouse;
    }

    public void setPickUpFromWarehouse(boolean pickUpFromWarehouse) {
        this.pickUpFromWarehouse = pickUpFromWarehouse;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
