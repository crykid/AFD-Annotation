package com.margin.afd_annotation;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.margin.afd_annotation.deserialization.Apple;
import com.margin.afd_annotation.deserialization.BaseResponse;
import com.margin.afd_annotation.deserialization.TypeRefrence;
import com.margin.afd_annotation.inject.Inject;
import com.margin.afd_annotation.inject.InjectUtils;

import java.lang.reflect.Type;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Inject(R.id.btn_main_function)
    Button btnFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtils.inject(this);
        btnFunction.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: ");
            SecondActivity.start(this, SecondActivity.ROLE_TYPE_BOSS, "张三", true);
        });


        serializable();
    }


    private void serializable() {

        Type type = new TypeRefrence<BaseResponse<Apple>>() {
        }.getType();

        Type type2 = new TypeToken<BaseResponse<Apple>>() {
        }.getType();

    }
}