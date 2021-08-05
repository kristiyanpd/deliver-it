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

        try (Session session = sessionFactory.openSession()) {
            var baseQuery = "select p from Parcel p join Shipment s on p.shipment.id = s.id ";
            List<String> filters = new ArrayList<>();

            if (warehouseId.isPresent()) {
                filters.add("s.originWarehouse.id or destinationWarehouse.id = :warehouseId");
            }
            if (weight.isPresent()) {
                filters.add("p.weight = :weight");
            }
            if (category.isPresent()) {
                filters.add("p.category like :category");
            }
            if (customerId.isPresent()) {
                filters.add("p.customerId = :customerId");
            }

            if (!filters.isEmpty()) {
                baseQuery += " where " + String.join(" and ", filters);
            }

            Query<Parcel> query = session.createQuery(baseQuery, Parcel.class);

            warehouseId.ifPresent(integer -> query.setParameter("warehouseId", integer));
            weight.ifPresent(aDouble -> query.setParameter("weight", aDouble));
            category.ifPresent(value -> query.setParameter("category", value));
            customerId.ifPresent(integer -> query.setParameter("customerId", integer));

            return query.list();
        }
    }

    @Override
    public List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate) {
        try (Session session = sessionFactory.openSession()) {

            var baseQuery = "select p from Parcel p join Shipment s on p.shipment.id = s.id ";
            if (weight.isPresent() && arrivalDate.isEmpty()) {
                baseQuery += " order by p.weight ";
            }
            if (weight.isEmpty() && arrivalDate.isPresent()) {
                baseQuery += " order by s.arrivalDate ";
            }
            if (weight.isPresent() && arrivalDate.isPresent()) {
                baseQuery += " order by p.weight,s.arrivalDate ";
            }

            Query<Parcel> query = session.createQuery(baseQuery, Parcel.class);

            return query.list();

        }

    }
}
