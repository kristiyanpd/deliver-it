package com.team9.deliverit.repositories;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.repositories.contracts.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public abstract class BaseRepositoryImpl<E> implements BaseRepository<E> {

    private final SessionFactory sessionFactory;

    public BaseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected abstract Class<E> getClazz();

    @Override
    public List<E> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<E> query = session.createQuery("from " + getClazz().getSimpleName(), getClazz());
            return query.list();
        }
    }

    @Override
    public E getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            E obj = session.get(getClazz(), id);
            if (obj == null) {
                throw new EntityNotFoundException(getClazz().getSimpleName(), id);
            }
            return obj;
        }
    }

    @Override
    public void create(E obj) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(obj);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(E obj) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(obj);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        E obj = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(obj);
            session.getTransaction().commit();
        }
    }
}
