package com.margin.afd_annotation.annotations;

import androidx.annotation.StringDef;

import com.margin.afd_annotation.SecondActivity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by : mr.lu
 * Created at : 2020/8/4 at 11:55
 * Description:
 */
@StringDef({SecondActivity.ROLE_TYPE_NORMAL, SecondActivity.ROLE_TYPE_BOSS})
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.SOURCE)
public @interface RoleType {

}
