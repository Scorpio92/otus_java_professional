package ru.otus.base;

import org.reflections.Reflections;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyGson {

    private final Set<JsonFieldRegister> registers;

    public MyGson() {
        registers = new HashSet<>();
    }

    public String toJson(Object o) {
        if (o == null) return null;

        if (registers.isEmpty()) initRegisters();

        JsonObjectBuilder builder = Json.createObjectBuilder();

        Arrays.stream(o.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            registers.forEach(register -> {
                try {
                    if (register.checkCondition(field.getType())) {
                        register.register(new FieldInfo(field.getName(), field.getType(), field.get(o)), builder);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Failed to process field [%s]", field.getName()), e);
                }
            });
        });

        return builder.build().toString();
    }

    private void initRegisters() {
        Reflections reflections = new Reflections("ru.otus.registers");
        reflections.getSubTypesOf(JsonFieldRegister.class).forEach(aClass -> {
            try {
                registers.add(aClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(String.format("Failed to init register [%s]", aClass.getSimpleName()), e);
            }
        });
    }
}
