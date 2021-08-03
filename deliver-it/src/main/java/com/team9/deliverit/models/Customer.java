package com.team9.deliverit.models;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "person_id")
    private PersonalDetails person;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PersonalDetails getPerson() {
        return person;
    }

    public void setPerson(PersonalDetails person) {
        this.person = person;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
