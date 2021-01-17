package ru.otus.jdbc.mapper;

public interface TableDataProvider {

    String tableName();

    Iterable<ColumnInfo> columns();
}
