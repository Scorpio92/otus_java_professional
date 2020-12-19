package ru.otus.registers;

import ru.otus.base.FieldInfo;
import ru.otus.base.JsonFieldRegister;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.math.BigDecimal;
import java.util.Collection;

public class CollectionRegister implements JsonFieldRegister {

    @Override
    public boolean checkCondition(Class<?> fieldType) {
        return Collection.class.isAssignableFrom(fieldType);
    }

    @Override
    public void register(FieldInfo fieldInfo, JsonObjectBuilder builder) {
        builder.add(fieldInfo.name(), Json.createArrayBuilder((Collection<?>) fieldInfo.value()));
    }
}
