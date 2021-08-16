package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.repositories.contracts.WarehouseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarehouseRepositoryImpl extends BaseRepositoryImpl<Warehouse> implements WarehouseRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public WarehouseRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Warehouse> getAll() {
        return super.getAll(Warehouse.class);
    }

    @Override
    public Warehouse getById(int id) {
        return super.getById(Warehouse.class, id);
    }

    @Override
    public void create(Warehouse warehouse) {
        super.create(Warehouse.class, warehouse);
    }

    @Override
    public void update(Warehouse warehouse) {
        super.update(Warehouse.class, warehouse);
    }

    @Override
    public void delete(int id) {
        super.delete(Warehouse.class, id);
    }

    @Override
    public boolean isDuplicate(int addressId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Warehouse> query = session.createQuery("from Warehouse where address.id = :id", Warehouse.class);
            query.setParameter("id", addressId);
            List<Warehouse> result = query.list();
            return result.size() > 0;
        }
    }

}

