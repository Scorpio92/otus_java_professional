package ru.otus;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

public class MyGson {

    public String toJson(Object o) {
        if (o == null) return null;

        JsonObjectBuilder builder = Json.createObjectBuilder();
        Class<?> c = o.getClass();

        Arrays.stream(c.getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);

            Class<?> fc = field.getType();
            String fieldName = field.getName();

            try {
                Object value = field.get(o);
                if (value != null) {
                    if (fc.isArray()) {
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                        Class<?> arrItemClazz = fc.getComponentType();
                        addToArrayBuilder(arrayBuilder, value, arrItemClazz);
                        builder.add(fieldName, arrayBuilder);
                    } else if (Collection.class.isAssignableFrom(fc)) {
                        builder.add(fieldName, Json.createArrayBuilder((Collection<?>) value));
                    } else {
                        if (fc == int.class || fc == Integer.class) {
                            builder.add(fieldName, (int) value);
                        } else if (fc == short.class || fc == Short.class) {
                            builder.add(fieldName, (short) value);
                        } else if (fc == long.class || fc == Long.class) {
                            builder.add(fieldName, (long) value);
                        } else if (fc == double.class || fc == Double.class) {
                            builder.add(fieldName, (double) value);
                        } else if (fc == float.class || fc == Float.class) {
                            builder.add(fieldName, (float) value);
                        } else if (fc == byte.class || fc == Byte.class) {
                            builder.add(fieldName, (byte) value);
                        } else if (fc == char.class || fc == Character.class) {
                            builder.add(fieldName, (char) value);
                        } else if (fc == boolean.class || fc == Boolean.class) {
                            builder.add(fieldName, (boolean) value);
                        } else if (fc == String.class) {
                            builder.add(fieldName, (String) value);
                        } else if (fc == BigInteger.class) {
                            builder.add(fieldName, (BigInteger) value);
                        } else if (fc == BigDecimal.class) {
                            builder.add(fieldName, (BigDecimal) value);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return builder.build().toString();
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
