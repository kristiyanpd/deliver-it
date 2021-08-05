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
    public List<City> getAll() {
        return super.getAll(City.class);
    }

    @Override
    public City getById(int id) {
        return super.getById(City.class, id);
    }

    @Override
    public void create(City city) {
        super.create(City.class, city);
    }

    @Override
    public void update(City city) {
        super.update(City.class, city);
    }

    @Override
    public void delete(int id) {
        super.delete(City.class, id);
    }

    @Override
    public City getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<City> query = session.createQuery("from City where name = :name", City.class);
            query.setParameter("name", name);
            List<City> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("City", "name", name);
            }
            return result.get(0);
        }
    }

}
