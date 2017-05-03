package com.amowel.dal;

import java.util.List;

/**
 * Created by amowel on 30.04.17.
 */
public interface CrudRepository<T> {
    List<T> findAll();

    T findById(Long id);

    T saveOrUpdate(T element);

    T delete(T element);
}
