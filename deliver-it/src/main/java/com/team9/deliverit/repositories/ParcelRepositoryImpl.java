package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ParcelRepositoryImpl extends BaseRepositoryImpl<Parcel> implements ParcelRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ParcelRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Parcel> getAll() {
        return super.getAll(Parcel.class);
    }

    @Override
    public Parcel getById(int id) {
        return super.getById(Parcel.class, id);
    }

    @Override
    public void create(Parcel parcel) {
        super.create(Parcel.class, parcel);
    }

    @Override
    public void update(Parcel parcel) {
        super.update(Parcel.class, parcel);
    }

    @Override
    public void delete(int id) {
        super.delete(Parcel.class, id);
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
            parcels = parcels.stream().filter(parcel -> parcel.getShipment().getDestinationWarehouse().getId() == warehouseId.get()).collect(Collectors.toList());
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
            Query<Parcel> query = session.createQuery("select p from Parcel p join Shipment s  on p.shipment.id = s.id order by s.arrivalDate", Parcel.class);
            return query.list();
        }
    }

    @Override
    public List<Parcel> sortByWeightAndArrivalDate() {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("select p from Parcel p join Shipment s on p.shipment.id = s.id order by p.weight,s.arrivalDate", Parcel.class);
            return query.list();
        }
    }

    @Override
    public List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate) {
        List<Parcel> output = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            if (weight.isPresent() && arrivalDate.isEmpty()) {
                Query<Parcel> query = session.createQuery("from Parcel order by weight", Parcel.class);
                output = query.list();
            }
            if (weight.isEmpty() && arrivalDate.isPresent()) {
                Query<Parcel> query = session.createQuery("select p from Parcel p join Shipment s  on p.shipment.id = s.id order by s.arrivalDate", Parcel.class);
                output = query.list();
            }
            if (weight.isPresent() && arrivalDate.isPresent()){
                Query<Parcel> query = session.createQuery("select p from Parcel p join Shipment s on p.shipment.id = s.id order by p.weight,s.arrivalDate", Parcel.class);
                return query.list();
            }
        }
        return output;
    }

}
