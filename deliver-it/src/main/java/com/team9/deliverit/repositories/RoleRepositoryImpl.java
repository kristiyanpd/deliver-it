package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Role;
import com.team9.deliverit.repositories.contracts.RoleRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends BaseRepositoryImpl<Role> implements RoleRepository {

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Role.class);
    }

}
