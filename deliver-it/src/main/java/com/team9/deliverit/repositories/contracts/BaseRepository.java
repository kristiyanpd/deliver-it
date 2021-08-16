package com.team9.deliverit.repositories.contracts;

import java.util.List;

public interface BaseRepository<E> {
    List<E> getAll();

    E getById(int id);

    void create(E obj);

    void update(E obj);

    void delete(int id);
}
