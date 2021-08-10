package com.team9.deliverit.models.enums;

import com.team9.deliverit.exceptions.StatusNotFoundException;

public enum Status {

    PREPARING("PREPARING"),
    ON_THE_WAY("ON_THE_WAY"),
    COMPLETED("COMPLETED");

    public static final String INVALID_STATUS = "Status should be PREPARING, ON_THE_WAY or COMPLETED";

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case PREPARING:
                return "Preparing";
            case ON_THE_WAY:
                return "On the way";
            case COMPLETED:
                return "Completed";
            default:
                throw new StatusNotFoundException(INVALID_STATUS);
        }
    }

    public static Status getEnum(String value) {
        for(Status v : values())
            if(v.getValue().equalsIgnoreCase(value)) return v;
        throw new StatusNotFoundException(INVALID_STATUS);
    }

}