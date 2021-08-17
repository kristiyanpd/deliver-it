package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EnumAlreadySameException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;
import com.team9.deliverit.models.enums.Status;
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
    protected Class<Parcel> getClazz() {
        return Parcel.class;
    }

    @Override
    public List<Parcel> getAllUserParcels(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery(
                    "select p from Parcel p where p.user.id = :userId", Parcel.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Parcel> pastParcels(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery(
                    "select p from Parcel p join Shipment s on p.shipment.id = s.id where p.user.id = :userId and s.status = 'COMPLETED'", Parcel.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Parcel> incomingParcels(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery(
                    "select p from Parcel p join Shipment s on p.shipment.id = s.id where p.user.id = :userId and s.status != 'COMPLETED'", Parcel.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public List<Parcel> filter(Optional<Double> weight,
                               Optional<Integer> warehouseId,
                               Optional<Category> category,
                               Optional<Status> status,
                               Optional<Integer> userId) {

        try (Session session = sessionFactory.openSession()) {
            var baseQuery = "select p from Parcel p join Shipment s on p.shipment.id = s.id ";
            List<String> filters = new ArrayList<>();

            if (warehouseId.isPresent()) {
                filters.add("s.originWarehouse.id = :warehouseId or destinationWarehouse.id = :warehouseId");
            }
            if (weight.isPresent()) {
                filters.add("p.weight = :weight");
            }
            if (category.isPresent()) {
                filters.add("p.category like :category");
            }
            if (status.isPresent()) {
                filters.add("p.status like :status");
            }
            if (userId.isPresent()) {
                filters.add("p.user.id = :userId");
            }

            if (!filters.isEmpty()) {
                baseQuery += " where " + String.join(" and ", filters);
            }

            Query<Parcel> query = session.createQuery(baseQuery, Parcel.class);

            warehouseId.ifPresent(integer -> query.setParameter("warehouseId", integer));
            weight.ifPresent(aDouble -> query.setParameter("weight", aDouble));
            category.ifPresent(value -> query.setParameter("category", value));
            status.ifPresent(value -> query.setParameter("status", value));
            userId.ifPresent(integer -> query.setParameter("userId", integer));

            return query.list();
        }
    }

    @Override
    public List<Parcel> sort(Optional<String> weight, Optional<String> arrivalDate, Optional<Integer> userId) {
        try (Session session = sessionFactory.openSession()) {
            var baseQuery = "select p from Parcel p join Shipment s on p.shipment.id = s.id ";

            if (userId.isPresent()) {
                baseQuery += " where p.user.id = :userId ";
            }
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
            userId.ifPresent(integer -> query.setParameter("userId", integer));

            return query.list();
        }

    }
}
