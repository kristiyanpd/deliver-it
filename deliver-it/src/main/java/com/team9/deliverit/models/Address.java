package com.team9.deliverit.models;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int id;

    @Column(name = "street_name")
    private String streetName;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public Address() {
    }

/*    public Address(int id, String streetName, City city) {
        setId(id);
        setStreetName(streetName);
        setCity(city);
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
