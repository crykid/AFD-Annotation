package com.margin.afd_annotation.inject;

import android.app.Activity;

import com.margin.afd_annotation.inject.annotation.AFDEventType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by : mr.lu
 * Created at : 2020/8/10 at 17:09
 * Description:
 */
public class InjetEvent {

    public static void inject(Activity activity) {
        Class<? extends Activity> activityClass = activity.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            Annotation[] annotations = declaredMethod.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(AFDEventType.class)) {
                    AFDEventType eventType = annotationType.getAnnotation(AFDEventType.class);
                    assert eventType != null;
                    Class listenerType = eventType.listenerType();
                    String listenerSetter = eventType.listenerSetter();

//                    Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType},handler)
                }
            }
        }
    }

//    public static void inject(Fragment fragment) {
//
//    }


} 