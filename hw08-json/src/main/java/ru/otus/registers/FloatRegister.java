package ru.otus.registers;

import ru.otus.base.FieldInfo;
import ru.otus.base.JsonFieldRegister;

import javax.json.JsonObjectBuilder;

public class FloatRegister implements JsonFieldRegister {

    @Override
    public boolean checkCondition(Class<?> fieldType) {
        return fieldType == float.class || fieldType == Float.class;
    }

    @Override
    public void register(FieldInfo fieldInfo, JsonObjectBuilder builder) {
        builder.add(fieldInfo.name(), (float) fieldInfo.value());
    }
}
