package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressRepositoryImpl extends BaseRepositoryImpl<Address> implements AddressRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public AddressRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Address.class);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Address> getDuplicate(String name, int cityId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery("from Address where streetName = :name and city.id = :cityId", Address.class);
            query.setParameter("name", name);
            query.setParameter("cityId", cityId);
            List<Address> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Address", "street name", name);
            }
            return result;
        }
    }

    public boolean isDuplicate(String name, int cityId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery("from Address where streetName = :name and city.id = :cityId", Address.class);
            query.setParameter("name", name);
            query.setParameter("cityId", cityId);
            List<Address> result = query.list();
            return result.size() > 0;
        }
    }

}
