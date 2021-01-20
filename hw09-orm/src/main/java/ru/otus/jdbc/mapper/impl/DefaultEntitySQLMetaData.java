package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntitySQLMetaData;

public class DefaultEntitySQLMetaData implements EntitySQLMetaData {

    @Override
    public String getSelectAllSql() {
        return "select * from %s";
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from %s where %s = %s";
    }

    @Override
    public String getInsertSql() {
        return "insert into %s (%s) values (%s)";
    }

    @Override
    public String getUpdateSql() {
        return "update %s set %s where %s = %s";
    }
}
