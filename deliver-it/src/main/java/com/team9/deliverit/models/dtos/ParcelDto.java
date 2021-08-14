package com.team9.deliverit.models.dtos;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class ParcelDto {

    @Positive(message = "Weight must be positive!")
    private double weight;

    @NotBlank(message = "Category can't be blank!")
    private String category;

//    @NotBlank(message = "Pick up option can't be blank!")
    private String pickUpOption;

    @Positive(message = "User ID must be positive!")
    private int userId;

    @Positive(message = "Shipment ID must be positive!")
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
