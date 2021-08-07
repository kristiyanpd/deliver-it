package com.team9.deliverit.models.enums;

public enum Category {

    ELECTRONICS("ELECTRONICS"),
    CLOTHING("CLOTHING"),
    MEDICAL("MEDICAL");

    public static final String INVALID_CATEGORY = "Pick up option should be ELECTRONICS, CLOTHING or MEDICAL";

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

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
                throw new IllegalArgumentException(INVALID_CATEGORY);
        }
    }

    public static Category getEnum(String value) {
        for(Category v : values())
            if(v.getValue().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException(INVALID_CATEGORY);
    }

}