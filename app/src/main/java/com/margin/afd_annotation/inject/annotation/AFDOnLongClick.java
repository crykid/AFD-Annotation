package com.margin.afd_annotation.inject.annotation;

import android.view.View;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by : mr.lu
 * Created at : 2020/8/10 at 10:25
 * Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@AFDEventType(listenerType = View.OnLongClickListener.class, listenerSetter = "setOnLongClickListener")
public @interface AFDOnLongClick {
    @IdRes int[] value();
}
