package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Parcel;
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
    public Shipment nextShipmentToArrive(int warehouseId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    "select s from Shipment s where s.destinationWarehouse.id = :warehouseId and s.status != 'COMPLETED' order by s.arrivalDate desc", Shipment.class);
            query.setParameter("warehouseId", warehouseId);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Shipment", "Destination warehouseId", String.valueOf(warehouseId));
            }
            return query.list().get(0);
        }
    }

    @Override
    public boolean isFull(int shipmentId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery("select s from Shipment s where s.id = :shipmentId", Shipment.class);
            query.setParameter("shipmentId", shipmentId);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("Shipment", "id", String.valueOf(shipmentId));
            }
            return query.list().get(0).isFull();
        }
    }

    @Override
    public boolean isEmpty(int shipmentId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("select s from Parcel s where s.shipment.id = :shipmentId", Parcel.class);
            query.setParameter("shipmentId", shipmentId);
            return query.list().size() == 0;
        }
    }


    @Override
    public int countShipmentsOnTheWay() {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery("select s from Shipment s where s.status = 'ON_THE_WAY'", Shipment.class);
            return query.list().size();
        }
    }

    @Override
    public List<Shipment> filter(Optional<Integer> warehouseId, Optional<Integer> userId) {
        try (Session session = sessionFactory.openSession()) {
/*            List<Shipment> output = new ArrayList<>();
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
            return output;*/

            var baseQuery = "select distinct s from Shipment s left join Parcel p on s.id = p.shipment.id ";

            if (warehouseId.isPresent() && userId.isEmpty()) {
                baseQuery += " where s.destinationWarehouse.id = :warehouseId or s.originWarehouse.id = :warehouseId ";
            }
            if (userId.isPresent() && warehouseId.isEmpty()) {
                baseQuery += " where p.user.id = :userId ";
            }

            if (userId.isPresent() && warehouseId.isPresent()){
                throw new IllegalArgumentException("You can filter only by warehouseId or customerId separately");
            }

            Query<Shipment> query = session.createQuery(baseQuery, Shipment.class);

            if (warehouseId.isPresent() && userId.isEmpty()) {
                query.setParameter("warehouseId", warehouseId.get());
            }

            if (userId.isPresent() && warehouseId.isEmpty()) {
                query.setParameter("userId", userId.get());
            }

            return query.list();

        }

    }
    //TODO VERIFY IF THAT WORKS
}
