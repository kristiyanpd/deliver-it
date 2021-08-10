package com.team9.deliverit.exceptions;

public class StatusAlreadySameException extends RuntimeException {

    public StatusAlreadySameException(String status) {
        super(String.format("Parcel pick up option is already %s!", status));
    }

}
