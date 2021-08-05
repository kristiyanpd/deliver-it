package com.team9.deliverit.models;

import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "parcels")
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parcel_id")
    private int id;


    @Positive(message = "Weight should be positive")
    @Column(name = "weight")
    private double weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "pick_up_option")
    private PickUpOption pickUpOption;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    public Parcel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PickUpOption getPickUpOption() {
        return pickUpOption;
    }

    public void setPickUpOption(PickUpOption pickUpOption) {
        this.pickUpOption = pickUpOption;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
