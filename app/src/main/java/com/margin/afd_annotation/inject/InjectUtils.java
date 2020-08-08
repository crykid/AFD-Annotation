package com.margin.afd_annotation.inject;

import android.app.Activity;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by : mr.lu
 * Created at : 2020/8/5 at 12:52
 * Description:
 */
public class InjectUtils {


    public static void inject(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
//        Intent intent = null;
//        ArrayMap<String, Object> mMapInstance = null;
        try {
            //获取所有的成员
            Field[] declaredFields = cls.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Inject inject = field.getAnnotation(Inject.class);
                    int viewId = inject.value();
                    View viewById = activity.findViewById(viewId);
                    field.setAccessible(true);
                    field.set(activity, viewById);
                }


                //-- 反射获取intent的字段
                //这种方式真的是走了好远的弯路- -，，，，
//                if (field.isAnnotationPresent(AutoWired.class)) {
//                    if (intent == null) {
//                        intent = activity.getIntent();
//
//                        intent.getIntExtra("null", 0);
//
//                        Bundle mExtrasBundle = intent.getExtras();
//                        if (mExtrasBundle == null) return;
//
//                        //bundle向上转型为BaseBundle，目的是获取其变量mMap
//                        Class<?> baseBundleClass = mExtrasBundle.getClass().getSuperclass();
//                        Field mMap = baseBundleClass.getDeclaredField("mMap");
//                        mMap.setAccessible(true);
//                        mMapInstance = (ArrayMap<String, Object>) mMap.get(mExtrasBundle);
//                    }
//
//                    if (mMapInstance != null) {
//                        AutoWired autoWired = field.getAnnotation(AutoWired.class);
//                        assert autoWired != null;
//                        String intentKey = autoWired.value();
//                        if (TextUtils.isEmpty(intentKey)) {
//                            intentKey = field.getName();
//                        }
//                        Object value = mMapInstance.get(intentKey);
//                        if (value != null) {
//                            field.setAccessible(true);
//                            field.set(activity, value);
//                        }
//                    }
//                    //---------------------
//
//                }
            }
//            intent = null;
//            mMapInstance = null;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进阶版
     *
     * @param activity
     */
    public static void autoWired(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
        //获取所有的成员
        Field[] declaredFields = cls.getDeclaredFields();
        Intent intent = activity.getIntent();
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        try {
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(AutoWired.class)) {
                    AutoWired autoWired = field.getAnnotation(AutoWired.class);
                    String intentKey = TextUtils.isEmpty(autoWired.value()) ? field.getName() : autoWired.value();
                    if (extras.containsKey(intentKey)) {
                        Object value = extras.get(intentKey);

                        //Parcelable数组不能直接设值，其他都可以
                        Class<?> componentType = field.getType().getComponentType();
                        if (field.getType().isArray() && Parcelable.class.isAssignableFrom(componentType)) {
                            Object[] objects = (Object[]) value;
                            Object[] parcelables = Arrays.copyOf(objects, objects.length, (Class<? extends java.lang.Object[]>) field.getType());
                            value = parcelables;
                        }
                        if (value != null) {
                            field.setAccessible(true);
                            field.set(activity, value);
                        }
                    }

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

} 