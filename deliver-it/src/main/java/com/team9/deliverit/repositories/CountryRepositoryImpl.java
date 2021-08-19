package com.team9.deliverit.repositories;

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
        super(sessionFactory, Country.class);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isDuplicate(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Country> query = session.createQuery("from Country where name = :name", Country.class);
            query.setParameter("name", name);
            List<Country> result = query.list();
            return result.size() > 0;
        }
    }

}
