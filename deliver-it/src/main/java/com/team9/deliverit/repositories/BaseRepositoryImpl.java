package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.repositories.contracts.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BaseRepositoryImpl<E> implements BaseRepository<E> {

    private final SessionFactory sessionFactory;

    public BaseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<E> getAll(Class<E> clazz) {
        try (Session session = sessionFactory.openSession()) {
            Query<E> query = session.createQuery("from " + clazz.getSimpleName(), clazz);
            return query.list();
        }
    }

    @Override
    public E getById(Class<E> clazz, int id) {
        try (Session session = sessionFactory.openSession()) {
            E obj = session.get(clazz, id);
            if (obj == null) {
                throw new EntityNotFoundException(clazz.getSimpleName(), id);
            }
            return obj;
        }
    }

    @Override
    public void create(Class<E> clazz, E obj) {
        try (Session session = sessionFactory.openSession()) {
            session.save(obj);
        }
    }

    @Override
    public void update(Class<E> clazz, E obj) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(obj);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Class<E> clazz, int id) {
        E obj = getById(clazz, id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(obj);
            session.getTransaction().commit();
        }
    }
}
