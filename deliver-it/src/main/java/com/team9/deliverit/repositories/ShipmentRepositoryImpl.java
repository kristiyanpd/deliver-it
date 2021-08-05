package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ShipmentRepositoryImpl extends BaseRepositoryImpl<Shipment> implements ShipmentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ShipmentRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Shipment> getAll() {
        return super.getAll(Shipment.class);
    }

    @Override
    public Shipment getById(int id) {
        return super.getById(Shipment.class, id);
    }

    @Override
    public void create(Shipment shipment) {
        super.create(Shipment.class, shipment);
    }

    @Override
    public void update(Shipment shipment) {
        super.update(Shipment.class, shipment);
    }

    @Override
    public void delete(int id) {
        super.delete(Shipment.class, id);
    }

    @Override
    public List<Shipment> filter(Optional<Integer> warehouseId, Optional<Integer> customerId) {
        try (Session session = sessionFactory.openSession()) {
            List<Shipment> output = new ArrayList<>();
            if (warehouseId.isPresent() && customerId.isEmpty()) {
                Query<Shipment> query = session.createQuery("from Shipment where destinationWarehouse.id = :warehouseId", Shipment.class);
                query.setParameter("warehouseId", warehouseId.get());
                output = query.list();
            }
            if (customerId.isPresent() && warehouseId.isEmpty()) {
                Query<Shipment> query = session.createQuery(
                        "select s from Shipment s left join Parcel p on s.id = p.shipment.id where p.customer.id = :customerId", Shipment.class);
                query.setParameter("customerId", customerId.get());
                output = query.list();
            }
            return output;
        }
    }

}
