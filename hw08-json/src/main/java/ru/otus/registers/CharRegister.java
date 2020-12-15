package ru.otus.registers;

import ru.otus.base.FieldInfo;
import ru.otus.base.JsonFieldRegister;

import javax.json.JsonObjectBuilder;

public class CharRegister implements JsonFieldRegister {

    @Override
    public boolean checkCondition(Class<?> fieldType) {
        return fieldType == char.class || fieldType == Character.class;
    }

    @Override
    public void register(FieldInfo fieldInfo, JsonObjectBuilder builder) {
        builder.add(fieldInfo.name(), (char) fieldInfo.value());
    }
}
