package ru.otus.jdbc.mapper;

public final class ColumnInfo {

    private final String name;
    private final Object value;
    private final boolean isId;

    public ColumnInfo(String name, Object value, boolean isId) {
        this.name = name;
        this.value = value;
        this.isId = isId;
    }

    public String name() {
        return name;
    }

    public Object value() {
        return value;
    }

    public boolean isId() {
        return isId;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", isId=" + isId +
                '}';
    }
}
