package com.team9.deliverit.models;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Positive(message = "ID should be a positive number!")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private int id;

   // Address address;

    public Warehouse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  /*  //public Address getAddress() {
        return address;
    }*/

  /*  public void setAddress(Address address) {
        this.address = address;
    }*/
}
