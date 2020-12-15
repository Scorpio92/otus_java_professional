package ru.otus.base;

import javax.json.JsonObjectBuilder;

public interface JsonFieldRegister {

    boolean checkCondition(Class<?> fieldType);

    void register(FieldInfo fieldInfo, JsonObjectBuilder builder);
}
