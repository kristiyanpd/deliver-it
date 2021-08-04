package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class ParcelRepositoryImpl implements ParcelRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ParcelRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Parcel> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("from Parcel", Parcel.class);
            return query.list();
        }
    }

    @Override
    public Parcel getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Parcel parcel = session.get(Parcel.class, id);
            if (parcel == null) {
                throw new EntityNotFoundException("Parcel", id);
            }
            return parcel;
        }
    }

    @Override
    public void create(Parcel parcel) {
        try (Session session = sessionFactory.openSession()) {
            session.save(parcel);
        }
    }

    @Override
    public void update(Parcel parcel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(parcel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Parcel parcelToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(parcelToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Parcel> filter(Optional<Double> weight, Optional<Integer> customerId,
                               Optional<Integer> warehouseId, Optional<Category> category) {
        List<Parcel> parcels = getAll();
        if (weight.isPresent()) {
            parcels = parcels.stream().filter(parcel -> parcel.getWeight() == weight.get()).collect(Collectors.toList());
        }
        if (customerId.isPresent()) {
            parcels = parcels.stream().filter(parcel -> parcel.getCustomer().getId() == customerId.get()).collect(Collectors.toList());
        }
        if (warehouseId.isPresent()) {
            parcels = parcels.stream().filter(parcel -> parcel.getWarehouse().getId() == warehouseId.get()).collect(Collectors.toList());
        }
        if (category.isPresent()) {
            parcels = parcels.stream().filter(parcel -> parcel.getCategory().equals(category.get())).collect(Collectors.toList());
        }
        return parcels;
    }

    @Override
    public List<Parcel> sortByWeight() {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("from Parcel ORDER BY weight", Parcel.class);
            return query.list();
        }
    }

    @Override
    public List<Parcel> sortByArrivalDate() {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("from Parcel left join Shipment s order by s.arrivalDate", Parcel.class);
            return query.list();
        }
    }

    @Override
    public List<Parcel> sortByWeightAndArrivalDate() {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("from Parcel p left join Shipment s order by p.weight,s.arrivalDate", Parcel.class);
            return query.list();
        }
    }
//TODO
}
