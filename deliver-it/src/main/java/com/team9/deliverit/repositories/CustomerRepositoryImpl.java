package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Customer;
import com.team9.deliverit.models.Parcel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Customer> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("from Customer", Customer.class);
            return query.list();
        }
    }

    @Override
    public Customer getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Customer customer = session.get(Customer.class, id);
            if (customer == null) {
                throw new EntityNotFoundException("Customer", id);
            }
            return customer;
        }
    }


    @Override
    public void create(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.save(customer);
        }
    }

    @Override
    public void update(Customer customer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(customer);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Customer customerToDelete = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(customerToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Customer> searchByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("select c from Customer c join PersonalDetails p on c.person.id = p.id where  p.email like concat('%',:email,'%')", Customer.class);
            query.setParameter("email", email);
            List<Customer> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Customer", "email", email);
            }
            return result;
        }
    }

    @Override
    public List<Customer> searchByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("select c from Customer c join PersonalDetails p on c.person.id = p.id where p.firstName = :name or p.lastName = :name", Customer.class);
            query.setParameter("name", name);
            List<Customer> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Customer", "name", name);
            }
            return result;
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

    //TODO SEARCH BY MULTIPLE CRITERIA

    @Override
    public List<Customer> searchAllFields(String param) {
        try (Session session = sessionFactory.openSession()) {
            Query<Customer> query = session.createQuery("select c from Customer c join PersonalDetails p on c.person.id = p.id where p.email like concat('%',:param,'%') or p.firstName like :param or p.lastName like :param", Customer.class);
            query.setParameter("param", param);
            List<Customer> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Customer", "param", param);
            }
            return result;
        }
    }
}
