package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.repositories.contracts.CountryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepositoryImpl extends BaseRepositoryImpl<Country> implements CountryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CountryRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> getAll() {
        return super.getAll(Country.class);
    }

    @Override
    public Country getById(int id) {
        return super.getById(Country.class, id);
    }

    @Override
    public void create(Country country) {
        super.create(Country.class, country);
    }

    @Override
    public void update(Country country) {
        super.update(Country.class, country);
    }

    @Override
    public void delete(int id) {
        super.delete(Country.class, id);
    }

    @Override
    public List<Country> searchByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("from Country where name like :name", Country.class);
            query.setParameter("name", "%" + name + "%");
            List<Country> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Country", "name", name);
            }
            return result;
        }
    }

    @Override
    public Country getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("from Country where name = :name", Country.class);
            query.setParameter("name", name);
            List<Country> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Country", "name", name);
            }
            return result.get(0);
        }
    }

    @Override
    public List<Country> getDuplicates(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("from Country where name = :name", Country.class);
            query.setParameter("name", name);
            List<Country> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Country", "name", name);
            }
            return result;
        }
    }

}
