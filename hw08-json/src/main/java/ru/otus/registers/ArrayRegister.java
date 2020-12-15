package ru.otus.registers;

import ru.otus.base.FieldInfo;
import ru.otus.base.JsonFieldRegister;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

public class ArrayRegister implements JsonFieldRegister {

    @Override
    public boolean checkCondition(Class<?> fieldType) {
        return fieldType.isArray();
    }

    @Override
    public void register(FieldInfo fieldInfo, JsonObjectBuilder builder) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Class<?> arrItemClazz = fieldInfo.type().getComponentType();
        addToArrayBuilder(arrayBuilder, fieldInfo.value(), arrItemClazz);
        builder.add(fieldInfo.name(), arrayBuilder);
    }

    private <T> void addToArrayBuilder(JsonArrayBuilder arrayBuilder, Object value, Class<T> tClass) {
        if (tClass == int.class) {
            for (int item : ((int[]) value)) arrayBuilder.add(item);
        }
        if (tClass == boolean.class) {
            for (boolean item : ((boolean[]) value)) arrayBuilder.add(item);
        }
        if (tClass == long.class) {
            for (long item : ((long[]) value)) arrayBuilder.add(item);
        }
        if (tClass == double.class) {
            for (double item : ((double[]) value)) arrayBuilder.add(item);
        }
        if (tClass == String.class) {
            for (String item : ((String[]) value)) arrayBuilder.add(item);
        }
        if (tClass == BigInteger.class) {
            for (BigInteger item : ((BigInteger[]) value)) arrayBuilder.add(item);
        }
        if (tClass == BigDecimal.class) {
            for (BigDecimal item : ((BigDecimal[]) value)) arrayBuilder.add(item);
        }
    }
}
