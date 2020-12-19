package ru.otus.registers;

import ru.otus.base.FieldInfo;
import ru.otus.base.JsonFieldRegister;

import javax.json.JsonObjectBuilder;

public class IntRegister implements JsonFieldRegister {

    @Override
    public boolean checkCondition(Class<?> fieldType) {
        return fieldType == int.class || fieldType == Integer.class;
    }

    @Override
    public void register(FieldInfo fieldInfo, JsonObjectBuilder builder) {
        builder.add(fieldInfo.name(), (int) fieldInfo.value());
    }
}
