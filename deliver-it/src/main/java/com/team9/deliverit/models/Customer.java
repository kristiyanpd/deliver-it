package com.team9.deliverit.models;

import javax.validation.constraints.Positive;

public class Customer {

    @Positive(message = "ID should be positive")
    private int id;

    private Person person;

    private Address address;

    public Customer(int id, Person person, Address address) {
        setId(id);
        setPerson(person);
        setAddress(address);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
