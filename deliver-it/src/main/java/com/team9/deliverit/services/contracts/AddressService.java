package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAll();

    Address getById(int id);

    Address getByName(String name);

    void create(Address address);

    void update(Address address);

    void delete(int id);

    List<Address> getDuplicates(String name,int cityId);
}
