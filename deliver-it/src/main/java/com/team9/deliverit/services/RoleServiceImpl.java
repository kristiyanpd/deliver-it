package com.team9.deliverit.services;

import com.team9.deliverit.models.Role;
import com.team9.deliverit.repositories.contracts.RoleRepository;
import com.team9.deliverit.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role getById(int id) {
        return repository.getById(id);
    }

}
