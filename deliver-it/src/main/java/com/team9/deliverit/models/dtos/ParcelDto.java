package com.team9.deliverit.models.dtos;

import javax.validation.constraints.Positive;

public class ParcelDto {

    @Positive(message = "Weight should be positive!")
    private double weight;

    private String category;

    private String pickUpOption;

    @Positive(message = "User id should be positive!")
    private int userId;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPickUpOption() {
        return pickUpOption;
    }

    public void setPickUpOption(String pickUpOption) {
        this.pickUpOption = pickUpOption;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }
}
