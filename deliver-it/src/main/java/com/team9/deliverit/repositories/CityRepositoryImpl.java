package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.repositories.contracts.CityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityRepositoryImpl extends BaseRepositoryImpl<City> implements CityRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CityRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    protected Class<City> getClazz() {
        return City.class;
    }

    @Override
    public List<City> getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery("from City where name like :name", City.class);
            query.setParameter("name", "%" + name + "%");
            List<City> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("City", "name", name);
            }
            return result;
        }
    }

    public boolean isDuplicate(String name, int countryId) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery("from City where name = :name and country.id = :countryId", City.class);
            query.setParameter("name", name);
            query.setParameter("countryId", countryId);
            List<City> result = query.list();
            return result.size() > 0;
        }
    }

}
