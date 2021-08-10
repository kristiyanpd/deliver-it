package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Address;

import java.util.List;

public interface AddressRepository {
    List<Address> getAll();

    Address getById(int id);

    List<Address> getByName(String name);

    void create(Address address);

    void update(Address address);

    void delete(int id);

    List <Address> getDuplicates (String name, int cityId);

    boolean isDuplicate(String name, int cityId);
}
