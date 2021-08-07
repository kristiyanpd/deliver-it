package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.StatusCompletedException;
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
    public List<Parcel> getAllUserParcels(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery(
                    "select p from Parcel p where p.user.id = :userId", Parcel.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    public String getStatusOfParcel(int parcelId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Shipment> query = session.createQuery(
                    "select s from Parcel p join Shipment s on p.shipment.id = s.id where p.id = :parcelId", Shipment.class);
            query.setParameter("parcelId", parcelId);
            return query.list().get(0).getStatus().toString();
        }
    }

    @Override
    public Parcel updatePickUpOption(int parcelId, PickUpOption pickUpOption) {
        try (Session session = sessionFactory.openSession()) {
            Parcel parcel = getById(parcelId);
            if (parcel.getShipment().getStatus() == Status.COMPLETED) {
                throw new StatusCompletedException(parcel.getId());
            }
            if (parcel.getPickUpOption() == pickUpOption) {
                throw new IllegalArgumentException(String.format("Parcel pick up option is already %s!", pickUpOption.toString().toLowerCase()));
            }
            session.beginTransaction();
            parcel.setPickUpOption(pickUpOption);
            session.update(parcel);
            session.getTransaction().commit();
            return parcel;
        }
    }

    @Override
    public List<Parcel> filter(Optional<Double> weight, Optional<Integer> userId,
                               Optional<Integer> warehouseId, Optional<Category> category) {

        try (Session session = sessionFactory.openSession()) {
            var baseQuery = "select p from Parcel p join Shipment s on p.shipment.id = s.id ";
            List<String> filters = new ArrayList<>();

            //TODO Add filterByUser method so users can filter their own classes
/*            if (!user.isEmployee()) {
                filters.add("p.userId = userId.")
            }*/

            if (warehouseId.isPresent()) {
                filters.add("s.originWarehouse.id = :warehouseId or destinationWarehouse.id = :warehouseId");
            }
            if (weight.isPresent()) {
                filters.add("p.weight = :weight");
            }
            if (category.isPresent()) {
                filters.add("p.category like :category");
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
            userId.ifPresent(integer -> query.setParameter("userId", integer));

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
