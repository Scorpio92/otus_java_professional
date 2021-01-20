package ru.otus.jdbc.mapper;

import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 * @param <T>
 */
public interface JdbcMapper<T> {
    long insert(T objectData);

    long update(T objectData);

    long insertOrUpdate(T objectData);

    Optional<T> findById(Object id, Class<T> clazz);
//    List<T> findAll(Class<T> clazz);
}
