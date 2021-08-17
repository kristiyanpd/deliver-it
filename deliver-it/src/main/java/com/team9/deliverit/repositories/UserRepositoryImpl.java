package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.UserRepository;
import com.team9.deliverit.services.contracts.RoleService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    protected Class<User> getClazz() {
        return User.class;
    }

    @Override
    public int countCustomers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("select u from User u where u.role.id = 1", User.class);
            return query.list().size();
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            if (query.list().size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return query.list().get(0);
        }
    }

    public boolean isDuplicate(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            return result.size() > 0;
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
    public List<User> searchEverywhere(String param) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email like concat('%',:param,'%') or firstName like :param or lastName like :param", User.class);
            query.setParameter("param", param);

            return query.list();
        }
    }

}
