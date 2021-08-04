package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Warehouse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public WarehouseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Warehouse> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery("from Warehouse ", Warehouse.class);
            return query.list();
        }
    }

    @Override
    public Warehouse getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Warehouse warehouse = session.get(Warehouse.class, id);
            if (warehouse == null) {
                throw new EntityNotFoundException("Warehouse", id);
            }
            return warehouse;
        }
    }

    @Override
    public Warehouse getByAddressId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery("from Warehouse where address.id = :id", Warehouse.class);
            query.setParameter("id", id);
            List<Warehouse> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Warehouse", id);
            }
            return result.get(0);
        }
    }


    @Override
    public void create(Warehouse warehouse) {
        try (Session session = sessionFactory.openSession()) {
            session.save(warehouse);
        }
    }


    @Override
    public void update(Warehouse warehouse) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(warehouse);
            session.getTransaction().commit();
        }
    }


    @Override
    public void delete(int id) {
        Warehouse warehouseToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(warehouseToDelete);
            session.getTransaction().commit();
        }
    }

}
