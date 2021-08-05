package com.team9.deliverit.repositories;

import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.repositories.contracts.CustomerRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepositoryImpl extends BaseRepositoryImpl<Customer> implements CustomerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomerRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Customer> getAll() {
        return super.getAll(Customer.class);
    }

    @Override
    public Customer getById(int id) {
        return super.getById(Customer.class, id);
    }

    @Override
    public void create(Customer customer) {
        super.create(Customer.class, customer);
    }

    @Override
    public void update(Customer customer) {
        super.update(Customer.class, customer);
    }

    @Override
    public void delete(int id) {
        super.delete(Customer.class, id);
    }

    @Override
    public List<Customer> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName) {

        try (Session session = sessionFactory.openSession()) {
            var baseQuery = "select c from Customer c join PersonalDetails p on c.person.id = p.id ";
            List<String> filters = new ArrayList<>();

            if (email.isPresent()) {
                filters.add(" p.email like concat('%',:email,'%') ");
            }
            if (firstName.isPresent()) {
                filters.add("p.firstName like :firstName");
            }
            if (lastName.isPresent()) {
                filters.add("p.lastName like :lastName");
            }

            if (!filters.isEmpty()) {
                baseQuery += " where " + String.join(" and ", filters);
            }

            Query<Customer> query = session.createQuery(baseQuery, Customer.class);

            email.ifPresent(s -> query.setParameter("email", s));
            firstName.ifPresent(s -> query.setParameter("firstName", s));
            lastName.ifPresent(s -> query.setParameter("lastName", s));

            return query.list();
        }
    }

    @Override
    public List<Parcel> incomingParcels(int customerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Parcel> query = session.createQuery("select p from Parcel p join Shipment s on p.shipment.id = s.id where p.customer = :customerId and s.status != 'COMPLETED'", Parcel.class);
            query.setParameter("customerId", customerId);
            return query.list();
        }
    }


    @Override
    public List<Customer> searchEverywhere(String param) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("select c from Customer c join PersonalDetails p on c.person.id = p.id where p.email like concat('%',:param,'%') or p.firstName like :param or p.lastName like :param", Customer.class);
            query.setParameter("param", param);

            return query.list();
        }
    }
    //TODO SEARCH AND FILTERS NOT TESTED
}
