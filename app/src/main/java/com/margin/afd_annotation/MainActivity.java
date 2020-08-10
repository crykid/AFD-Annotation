package com.margin.afd_annotation;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.margin.afd_annotation.deserialization.Apple;
import com.margin.afd_annotation.deserialization.BaseResponse;
import com.margin.afd_annotation.deserialization.TypeRefrence;
import com.margin.afd_annotation.inject.annotation.AFDOnClick;
import com.margin.afd_annotation.inject.annotation.Inject;
import com.margin.afd_annotation.inject.InjectUtils;

import java.io.Serializable;
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
            SecondActivity.start(this, SecondActivity.ROLE_TYPE_BOSS, "张三", true, new Computer(),new CellPhone());
        });


        serializable();
    }

    public static class Computer implements Serializable {
        public String system = "windows";

    }

    public static class CellPhone implements Parcelable {
        public String system = "android";

        public CellPhone() {
        }

        protected CellPhone(Parcel in) {
            system = in.readString();
        }

        public static final Creator<CellPhone> CREATOR = new Creator<CellPhone>() {
            @Override
            public CellPhone createFromParcel(Parcel in) {
                return new CellPhone(in);
            }

            @Override
            public CellPhone[] newArray(int size) {
                return new CellPhone[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(system);
        }
    }

    @AFDOnClick({R.id.btn_main_afdonclick})
    public void onViewClick(View view) {
        Log.d(TAG, "onViewClick: view = " + view.getId());
    }

    private void serializable() {

        Type type = new TypeRefrence<BaseResponse<Apple>>() {
        }.getType();

        Type type2 = new TypeToken<BaseResponse<Apple>>() {
        }.getType();

    }
}