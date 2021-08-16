package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Role;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl extends BaseRepositoryImpl<Role> implements com.team9.deliverit.repositories.contracts.RoleRepository {

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Class<Role> getClazz() {
        return Role.class;
    }

}
