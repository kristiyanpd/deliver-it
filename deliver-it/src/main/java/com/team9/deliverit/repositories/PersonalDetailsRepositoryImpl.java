package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.PersonalDetails;
import com.team9.deliverit.models.Shipment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import java.util.List;

@Repository
public class PersonalDetailsRepositoryImpl implements PersonalDetailsRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonalDetailsRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PersonalDetails getByEmail(@Email String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<PersonalDetails> query = session.createQuery("from PersonalDetails where email = :email", PersonalDetails.class);
            query.setParameter("email", email);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("PersonalDetails", "email", email);
            }
            return query.list().get(0);
        }
    }

    @Override
    public PersonalDetails getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            PersonalDetails personalDetails = session.get(PersonalDetails.class, id);
            if (personalDetails == null) {
                throw new EntityNotFoundException("PersonalDetails", id);
            }
            return personalDetails;
        }
    }

    @Override
    public void create(PersonalDetails personalDetails) {
        try (Session session = sessionFactory.openSession()) {
            session.save(personalDetails);
        }
    }


    @Override
    public void update(PersonalDetails personalDetails) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(personalDetails);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        PersonalDetails personalDetails = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(personalDetails);
            session.getTransaction().commit();
        }
    }

}
