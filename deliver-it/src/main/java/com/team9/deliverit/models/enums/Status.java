package com.team9.deliverit.models.enums;

import com.team9.deliverit.exceptions.StatusNotFoundException;

public enum Status {
    PREPARING,
    ON_THE_WAY,
    COMPLETED;

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
                throw new StatusNotFoundException("Status must be Preparing, On the way or Completed!");
        }
    }
}