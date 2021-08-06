package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.PersonalDetails;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import java.util.List;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        return super.getAll(User.class);
    }

    @Override
    public User getById(int id) {
        return super.getById(User.class, id);
    }

    @Override
    public void create(User user) {
        super.create(User.class, user);
    }

    @Override
    public void update(User user) {
        super.update(User.class, user);
    }

    @Override
    public void delete(int id) {
        super.delete(User.class, id);
    }

    @Override
    public User getByEmail(@Email String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return query.list().get(0);
        }
    }

}
