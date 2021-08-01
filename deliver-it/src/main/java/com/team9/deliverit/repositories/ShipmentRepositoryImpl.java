package com.team9.deliverit.repositories;


import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ShipmentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Shipment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery("from Shipment", Shipment.class);
            return query.list();
        }
    }

    @Override
    public Shipment getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Shipment shipment = session.get(Shipment.class, id);
            if (shipment == null) {
                throw new EntityNotFoundException("Shipment", id);
            }
            return shipment;
        }
    }

    @Override
    public void create(Shipment shipment) {
        try (Session session = sessionFactory.openSession()) {
            session.save(shipment);
        }
    }

    @Override
    public void update(Shipment shipment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(shipment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Shipment shipmentToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(shipmentToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Shipment> filterByDestinationWarehouse(int warehouseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery("from Shipment where destinationWarehouse.id = :warehouseId", Shipment.class);
            query.setParameter("warehouseId", warehouseId);
            return query.list();
        }
    }

    @Override
    public List<Shipment> filterByCustomer(int customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("from Parcel where customer.id = :customerId",Parcel.class);
            query.setParameter("customerId", customerId);
            return query.list().stream().map(Parcel::getShipment).collect(Collectors.toList());
        }
    }

}
