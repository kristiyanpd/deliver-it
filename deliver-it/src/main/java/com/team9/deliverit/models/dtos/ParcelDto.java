package com.team9.deliverit.models.dtos;

import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;

import javax.validation.constraints.Positive;

public class ParcelDto {

    @Positive(message = "Weight should be positive!")
    private double weight;

    private Category category;

    private PickUpOption pickUpOption;

    @Positive(message = "Customer id should be positive!")
    private int customerId;

    @Positive(message = "Shipment id should be positive!")
    private int shipmentId;

    public ParcelDto() {

    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public PickUpOption getPickUpOption() {
        return pickUpOption;
    }

    public void setPickUpOption(PickUpOption pickUpOption) {
        this.pickUpOption = pickUpOption;
    }
}
