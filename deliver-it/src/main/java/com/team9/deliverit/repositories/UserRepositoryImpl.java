package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.UserRepository;
import com.team9.deliverit.services.contracts.RoleService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {

    private final SessionFactory sessionFactory;
    private final RoleService roleService;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory, RoleService roleService) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
        this.roleService = roleService;
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
    public int countCustomers(){
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("select u from User u where u.role.id = 1", User.class);
            return query.list().size();
        }
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

    @Override
    public User registerEmployee(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = getById(id);
            session.beginTransaction();
            user.setRole(roleService.getById(2));
            session.update(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public List<User> search(Optional<String> email, Optional<String> firstName, Optional<String> lastName) {

        try (Session session = sessionFactory.openSession()) {
            var baseQuery = "select u from User u ";
            List<String> filters = new ArrayList<>();

            if (email.isPresent()) {
                filters.add(" u.email like concat('%',:email,'%') ");
            }
            if (firstName.isPresent()) {
                filters.add(" u.firstName like :firstName");
            }
            if (lastName.isPresent()) {
                filters.add(" u.lastName like :lastName");
            }

            if (!filters.isEmpty()) {
                baseQuery += " where " + String.join(" and ", filters);
            }

            Query<User> query = session.createQuery(baseQuery, User.class);

            email.ifPresent(s -> query.setParameter("email", s));
            firstName.ifPresent(s -> query.setParameter("firstName", s));
            lastName.ifPresent(s -> query.setParameter("lastName", s));

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
    public List<User> searchEverywhere(String param) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("select u from User u where u.email like concat('%',:param,'%') or u.firstName like :param or u.lastName like :param", User.class);
            query.setParameter("param", param);

            return query.list();
        }
    }
    //TODO SEARCH AND FILTERS NOT TESTED

}
