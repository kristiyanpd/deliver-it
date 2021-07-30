package com.team9.deliverit.models;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "employee")
public class Employee {

    @Positive(message = "ID should be positive")
    @Positive(message = "ID should be positive")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    public Employee() {

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
