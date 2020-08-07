package com.margin.afd_annotation.inject;

import android.app.Activity;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by : mr.lu
 * Created at : 2020/8/5 at 12:52
 * Description:
 */
public class InjectUtils {

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

                if (field.isAnnotationPresent(AutoWired.class)) {
                    final Intent intent = activity.getIntent();
                    AutoWired autoWired = field.getAnnotation(AutoWired.class);
                    assert autoWired != null;
                    String intentKey = autoWired.value();
                    if (TextUtils.isEmpty(intentKey)) {
                        intentKey = field.getName();
                    }

                    Object value = null;
                    //获取Intent里的Bundle mExtras
                    Class<?> intentClass = Class.forName("android.content.Intent");
                    Field mExtras = intentClass.getDeclaredField("mExtras");
                    if (mExtras == null) return;
                    mExtras.setAccessible(true);
                    Bundle mExtrasBundle = (Bundle) mExtras.get(intent);

                    if (mExtrasBundle == null) return;

                    //bundle向上转型为BaseBundle，目的是获取其变量mMap
                    BaseBundle baseBundle = mExtrasBundle;
                    //获取mExtras里面的 ArrayMap<String, Object> mMap
                    Class<?> baseBundleClass = Class.forName("android.os.BaseBundle");
                    Field mMap = baseBundleClass.getDeclaredField("mMap");
                    mMap.setAccessible(true);
                    ArrayMap<String, Object> mMapInstance = (ArrayMap<String, Object>) mMap.get(baseBundle);

                    if (mMapInstance != null) {
                        value = mMapInstance.get(intentKey);
                        field.setAccessible(true);
                        field.set(activity, value);
                    }

//                    Class<?> type = field.getType();
//                    if (type == int.class) {
//                        value = intent.getIntExtra(intentKey, 0);
//                    } else if (type == String.class) {
//                        value = intent.getStringExtra(intentKey);
//                    } else if (type == boolean.class) {
//                        value = intent.getBooleanExtra(intentKey, false);
//                    }

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
} 