package ru.otus.base;

public final class FieldInfo {

    private final String name;
    private final Class<?> type;
    private final Object value;

    public FieldInfo(String name, Class<?> type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public Class<?> type() {
        return type;
    }

    public Object value() {
        return value;
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
