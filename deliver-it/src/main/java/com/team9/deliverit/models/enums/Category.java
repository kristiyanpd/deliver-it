package com.team9.deliverit.models.enums;

import com.team9.deliverit.exceptions.EnumNotFoundException;

public enum Category {

    ELECTRONICS("ELECTRONICS"),
    CLOTHING("CLOTHING"),
    MEDICAL("MEDICAL");

    public static final String INVALID_CATEGORY = "Category should be ELECTRONICS, CLOTHING or MEDICAL";

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
                throw new EnumNotFoundException(INVALID_CATEGORY);
        }
    }

    public static Category getEnum(String value) {
        for (Category v : values())
            if (v.getValue().equalsIgnoreCase(value)) return v;
        throw new EnumNotFoundException(INVALID_CATEGORY);
    }

}