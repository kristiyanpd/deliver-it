package com.team9.deliverit.models.enums;

public enum Category {
    ELECTRONICS,
    CLOTHING,
    MEDICAL;

    @Override
    public String toString() {
        switch (this) {
            case MEDICAL:
                return "Medical";
            case CLOTHING:
                return "Clothing";
            case ELECTRONICS:
                return "Electronics";
            default:
                throw new IllegalArgumentException("Category should be Electronics, Clothing or Medical");
        }
    }
}
