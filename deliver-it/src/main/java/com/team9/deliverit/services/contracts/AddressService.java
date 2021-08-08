package com.team9.deliverit.services.contracts;

import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.AddressDisplayDto;

import java.util.List;

public interface AddressService {

    List<AddressDisplayDto> getAll(User user);

    Address getById(User user, int id);

    Address getByName(User user, String name);

    void create(User user, Address address);

    void update(User user, Address address);

    void delete(User user, int id);

    List<Address> getDuplicates(String name, int cityId);
}
