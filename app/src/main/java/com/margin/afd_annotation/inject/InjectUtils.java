package com.margin.afd_annotation.inject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;

import com.margin.afd_annotation.inject.annotation.AFDOnClick;
import com.margin.afd_annotation.inject.annotation.AutoWired;
import com.margin.afd_annotation.inject.annotation.Inject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by : mr.lu
 * Created at : 2020/8/5 at 12:52
 * Description:
 */
public class InjectUtils {
    private final static Map<Integer, Method> VIEW_METHOD_CACHE = new HashMap<>();
    private final static Map<Class, View.OnClickListener> CONTEXT_LISTENER_CACHE = new HashMap<>();

    public static void inject(Activity activity) {
        Class<? extends Activity> cls = activity.getClass();
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
//            }
            handleOnClick(activity, cls);

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


    /**
     * 找到有注解的目标方法。
     * 返回的是一个列表，因为一个类里面可能有多个方法被注解
     *
     * @param declaredMethods
     * @return
     */
    private static List<Method> findTargetMethod(Method[] declaredMethods) {
        List<Method> methods = new ArrayList<>();
        for (Method method : declaredMethods) {
            //1.首先找到被注解的方法
            if (method.isAnnotationPresent(AFDOnClick.class)) {
                //2.方法要是public的
                int modifiers = method.getModifiers();
                if ((modifiers & Modifier.PUBLIC) != 0) {
                    //3.方法第一个参数要是View
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    //方法第一个参数类型一定要是View
                    if (parameterTypes.length > 0) {
                        if (method.getParameterTypes()[0].getName().equals("android.view.View")) {
                            methods.add(method);
                        } else {
                            throw new IllegalStateException("The first parameter to this method must be of View type ！");
                        }
                    }

                }

            }
        }
        return methods;
    }


    /**
     * 注解，点击事件
     *
     * @param activity
     * @param cls
     */
    private static void handleOnClick(Activity activity, Class<? extends Activity> cls) {
        Method[] declaredMethods = cls.getDeclaredMethods();
        if (declaredMethods.length == 0) return;

        //找到注解对应的方法
        final List<Method> methods = findTargetMethod(declaredMethods);
        if (methods.size() == 0) {
            return;
        }
        //找到这个类的listener
        final View.OnClickListener proxyOnClickListener = loadOnClickListener(activity, cls);

        //每个view设置点击事件
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            AFDOnClick annotation = method.getAnnotation(AFDOnClick.class);
            assert annotation != null;
            //每个方法上的id
            int[] singleMethodIds = annotation.value();
            for (int id : singleMethodIds) {
                View view = activity.findViewById(id);
                //添加到缓存
                VIEW_METHOD_CACHE.put(id, method);
                view.setOnClickListener(proxyOnClickListener);
            }
        }


    }

    /**
     * 获取当前类里面的listener，防止重复创建
     *
     * @param activity
     * @param cls
     * @return
     */
    private static View.OnClickListener loadOnClickListener(Object activity, Class<?> cls) {
        //每个activity或者fragment或者其他类型的类里面，只有一个就好了
        View.OnClickListener proxyOncLickListener = CONTEXT_LISTENER_CACHE.get(cls);
        //要根据点击的View区分调用的方法
        if (proxyOncLickListener == null) {
            proxyOncLickListener = (View.OnClickListener) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    final View view = (View) args[0];
                    final Method targetMethod = VIEW_METHOD_CACHE.get(view.getId());
                    return targetMethod.invoke(activity, args);
                }
            });
            CONTEXT_LISTENER_CACHE.put(cls, proxyOncLickListener);
        }
        return proxyOncLickListener;
    }


}