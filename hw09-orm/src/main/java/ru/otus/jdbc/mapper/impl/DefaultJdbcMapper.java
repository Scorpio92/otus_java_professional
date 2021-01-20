package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultJdbcMapper<T> implements JdbcMapper<T> {

    private final SessionManagerJdbc sessionManager;
    private final DbExecutor<T> dbExecutor;

    public DefaultJdbcMapper(SessionManagerJdbc sessionManager, DbExecutor<T> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public long insert(T objectData) {
        String query = sqlMetaData().getInsertSql();

        EntityClassMetaData<T> classMetaData = classMetaData((Class<T>) objectData.getClass());
        Supplier<Stream<Field>> allFields = () -> classMetaData.getAllFields().stream();

        String tableName = classMetaData.getName();

        String columns = allFields.get()
                .map(field -> {
                    field.setAccessible(true);
                    return field.getName();
                })
                .collect(Collectors.joining(", "));

        String valuesString = allFields.get()
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        List<Object> values = allFields.get()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(objectData);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        query = String.format(query, tableName, columns, valuesString);
        System.out.println(query);

        try {
            return dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(), query, values);
        } catch (SQLException se) {
            throw new RuntimeException("Failed to insert new record", se);
        }
    }

    @Override
    public long update(T objectData) {
        String query = sqlMetaData().getUpdateSql();

        EntityClassMetaData<T> classMetaData = classMetaData((Class<T>) objectData.getClass());
        Field idField = classMetaData.getIdField();
        idField.setAccessible(true);
        Supplier<Stream<Field>> allFields = () -> classMetaData.getAllFields().stream();

        String tableName = classMetaData.getName();

        String idFieldName = idField.getName();
        String idFieldValue;
        try {
             idFieldValue = String.valueOf(idField.get(objectData));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to update record", e);
        }

        String set = allFields.get()
                .map(field -> {
                    field.setAccessible(true);
                    return String.format("%s = ?", field.getName());
                })
                .collect(Collectors.joining(", "));

        List<Object> values = allFields.get()
                .map(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(objectData);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        values.add(idFieldValue);

        query = String.format(query, tableName, set, idFieldName, "?");
        System.out.println(query);

        try {
            return dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(), query, values);
        } catch (SQLException se) {
            throw new RuntimeException("Failed to insert new record", se);
        }
    }

    @Override
    public long insertOrUpdate(T objectData) {
        Class<T> tClass = (Class<T>) objectData.getClass();
        EntityClassMetaData<T> classMetaData = classMetaData(tClass);
        Field idField = classMetaData.getIdField();
        idField.setAccessible(true);
        String idFieldValue;
        try {
            idFieldValue = String.valueOf(idField.get(objectData));
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to insertOrUpdate record", e);
        }

        if (findById(idFieldValue, tClass).isPresent()) {
            return update(objectData);
        } else {
            return insert(objectData);
        }
    }

    @Override
    public Optional<T> findById(Object id, Class<T> clazz) {
        String query = sqlMetaData().getSelectByIdSql();

        EntityClassMetaData<T> classMetaData = classMetaData(clazz);
        Field idField = classMetaData.getIdField();
        idField.setAccessible(true);

        String tableName = classMetaData.getName();
        String idFieldName = idField.getName();

        query = String.format(query, tableName, idFieldName, "?");
        System.out.println(query);

        try {
            return dbExecutor.executeSelect(sessionManager.getCurrentSession().getConnection(), query, id, rs -> {
                try {
                    if (rs.next()) {
                        List<Object> params = new ArrayList<>();
                        for (Field field : classMetaData.getAllFields()) {
                            field.setAccessible(true);
                            Class<?> type = field.getType();
                            if (type == String.class) {
                                params.add(rs.getString(field.getName()));
                            }
                            if (type == long.class || type == Long.class) {
                                params.add(rs.getLong(field.getName()));
                            }
                            if (type == int.class || type == Integer.class) {
                                params.add(rs.getInt(field.getName()));
                            }
                            if (type == double.class || type == Double.class) {
                                params.add(rs.getDouble(field.getName()));
                            }
                        }
                        try {
                            return classMetaData.getConstructor().newInstance(params.toArray());
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to get instance of class", e);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to select record by id", e);
                }
                return null;
            });
        } catch (SQLException se) {
            throw new RuntimeException("Failed to select record by id", se);
        }
    }

    private EntityClassMetaData<T> classMetaData(Class<T> clazz) {
        return new DefaultEntityClassMetaData<>(clazz);
    }

    private EntitySQLMetaData sqlMetaData() {
        return new DefaultEntitySQLMetaData();
    }
}
