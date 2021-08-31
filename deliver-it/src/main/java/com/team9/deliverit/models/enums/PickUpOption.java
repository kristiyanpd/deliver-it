package com.team9.deliverit.models.enums;

import com.team9.deliverit.exceptions.EnumNotFoundException;

public enum PickUpOption {

    PICK_UP_FROM_WAREHOUSE("Pick up from warehouse"),
    DELIVER_TO_ADDRESS("Deliver to address");

    public static final String INVALID_PICKUP_OPTION = "Pick up option should be PICK_UP_FROM_WAREHOUSE or DELIVER_TO_ADDRESS";

    private final String value;

    PickUpOption(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case PICK_UP_FROM_WAREHOUSE:
                return "Pick up from warehouse";
            case DELIVER_TO_ADDRESS:
                return "Deliver to address";
            default:
                throw new EnumNotFoundException(INVALID_PICKUP_OPTION);
        }
    }

    public static PickUpOption getEnum(String value) {
        for (PickUpOption v : values())
            if (v.getValue().equalsIgnoreCase(value)) return v;
        throw new EnumNotFoundException(INVALID_PICKUP_OPTION);
    }

}
