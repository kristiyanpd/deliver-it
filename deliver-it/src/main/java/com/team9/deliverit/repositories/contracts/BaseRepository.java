package com.team9.deliverit.repositories.contracts;

import java.util.List;

public interface BaseRepository<E> {
    List<E> getAll(Class<E> clazz);

    E getById(Class<E> clazz, int id);

    void create(Class<E> clazz, E obj);

    void update(Class<E> clazz, E obj);

    void delete(Class<E> clazz, int id);
}
