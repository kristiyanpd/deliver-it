package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Role;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.UserRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl extends BaseRepositoryImpl<Role> implements com.team9.deliverit.repositories.contracts.RoleRepository {

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Role getById(int id) {
        return super.getById(Role.class, id);
    }

}
