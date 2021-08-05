package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.PersonalDetails;
import com.team9.deliverit.repositories.contracts.PersonalDetailsRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;

@Repository
public class PersonalDetailsRepositoryImpl extends BaseRepositoryImpl<PersonalDetails> implements PersonalDetailsRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonalDetailsRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(PersonalDetails personalDetails) {
        super.create(PersonalDetails.class, personalDetails);
    }

    @Override
    public void update(PersonalDetails personalDetails) {
        super.update(PersonalDetails.class, personalDetails);
    }

    @Override
    public void delete(int id) {
        super.delete(PersonalDetails.class, id);
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

}
