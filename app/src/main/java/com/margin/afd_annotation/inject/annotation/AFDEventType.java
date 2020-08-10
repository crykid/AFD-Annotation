package com.margin.afd_annotation.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by : mr.lu
 * Created at : 2020/8/10 at 16:52
 * Description:
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AFDEventType {
    Class listenerType();

    String listenerSetter();
}
