package com.team9.deliverit.repositories.contracts;

import com.team9.deliverit.models.Address;

import java.util.List;

public interface AddressRepository extends BaseRepository<Address> {

    List<Address> getDuplicate(String name, int cityId);

    boolean isDuplicate(String name, int cityId);
}
