package ru.otus.registers;

import ru.otus.base.FieldInfo;
import ru.otus.base.JsonFieldRegister;

import javax.json.JsonObjectBuilder;
import java.math.BigInteger;

public class BigIntegerRegister implements JsonFieldRegister {

    @Override
    public boolean checkCondition(Class<?> fieldType) {
        return fieldType == BigInteger.class;
    }

    @Override
    public void register(FieldInfo fieldInfo, JsonObjectBuilder builder) {
        builder.add(fieldInfo.name(), (BigInteger) fieldInfo.value());
    }
}
