package com.margin.afd_annotation.deserialization;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeRefrence<T> {
    Type type;
    T t;

    protected TypeRefrence() {
    }

    public Type getType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        assert parameterizedType != null;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        type = actualTypeArguments[0];
        return type;
    }
}