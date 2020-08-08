package com.margin.afd_annotation;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.margin.afd_annotation.annotations.RoleType;
import com.margin.afd_annotation.inject.AutoWired;
import com.margin.afd_annotation.inject.InjectUtils;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

    private final static String INTENT_ROLE_TYPE = "intent_role_type";
    private final static String INTENT_NAME = "intent_name";
    private final static String INTENT_BOY = "intent_boy";

    public final static String ROLE_TYPE_NORMAL = "emploee";
    public final static String ROLE_TYPE_BOSS = "boss";


    @AutoWired(INTENT_ROLE_TYPE)
    private String position;
    @AutoWired(INTENT_NAME)
    private String name;
    @AutoWired(ROLE_TYPE_BOSS)
    private boolean isBoy;

    @AutoWired
    private MainActivity.Computer computer;

    @AutoWired
    MainActivity.CellPhone cellPhone;


    public static void start(Activity activity, @RoleType String roleType, String name, boolean boy, MainActivity.Computer computer, MainActivity.CellPhone phone) {
        activity.startActivity(new Intent(activity, SecondActivity.class)
                .putExtra(INTENT_ROLE_TYPE, roleType)
                .putExtra(INTENT_NAME, name)
                .putExtra(INTENT_BOY, boy)
                .putExtra("computer", computer)
                .putExtra("cellPhone", phone)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        InjectUtils.inject(this);
        InjectUtils.autoWired(this);

        Log.d(TAG, "onCreate: position = " + position + ",name = " + name + " , isBoy =" + isBoy+ "\n"
                + " Serializable : computer = " + computer.system
                + " Parcelable : phone = " + cellPhone.system);

    }


}