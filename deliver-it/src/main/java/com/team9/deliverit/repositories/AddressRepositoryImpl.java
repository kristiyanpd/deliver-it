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
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Address> getAll() {
        return super.getAll(Address.class);
    }

    @Override
    public Address getById(int id) {
        return super.getById(Address.class, id);
    }

    @Override
    public void create(Address address) {
        super.create(Address.class, address);
    }

    @Override
    public void update(Address address) {
        super.update(Address.class, address);
    }

    @Override
    public void delete(int id) {
        super.delete(Address.class, id);
    }

    @Override
    public Address getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery("from Address where streetName = :name", Address.class);
            query.setParameter("name", name);
            List<Address> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Address", "street name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public List <Address> getDuplicates (String name, int cityId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Address> query = session.createQuery("from Address where streetName = :name and city.id = :cityId", Address.class);
            query.setParameter("name", name);
            query.setParameter("cityId", cityId);
            List<Address> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Address","street name",name);
            }
            return result;
        }
    }

}
