package com.team9.deliverit.exceptions;

public class StatusCompletedException extends RuntimeException {

    public StatusCompletedException(int id) {
        this(String.valueOf(id));
    }

    public StatusCompletedException(String value) {
        super(String.format("Parcel with %s is already Completed !", value));
    }

}
