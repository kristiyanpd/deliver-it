package com.team9.deliverit.models.dtos;

import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;


public class ParcelDisplayDto {

    private int id;

    private double weight;

    private Category category;

    private PickUpOption pickUpOption;

    private UserDisplayDto user;

    private AddressDisplayDto destinationAddress;

    public ParcelDisplayDto() {

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public PickUpOption getPickUpOption() {
        return pickUpOption;
    }

    public void setPickUpOption(PickUpOption pickUpOption) {
        this.pickUpOption = pickUpOption;
    }

    public UserDisplayDto getUser() {
        return user;
    }

    public void setUser(UserDisplayDto user) {
        this.user = user;
    }

    public AddressDisplayDto getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(AddressDisplayDto destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
}
