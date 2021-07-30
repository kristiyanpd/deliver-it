package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Address;

import java.util.List;

public interface AddressRepository {
    List<Address> getAll();

    Address getById(int id);
}
