package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepositoryImpl implements CountryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CountryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("from Country", Country.class);
            return query.list();
        }
    }

    @Override
    public Country getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Country country = session.get(Country.class, id);
            if (country == null) {
                throw new EntityNotFoundException("Country", id);
            }
            return country;
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
    public void create(Country country) {
        try (Session session = sessionFactory.openSession()) {
            session.save(country);
        }
    }

    @Override
    public void update(Country country) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(country);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Country CountryToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(CountryToDelete);
            session.getTransaction().commit();
        }
    }

}
