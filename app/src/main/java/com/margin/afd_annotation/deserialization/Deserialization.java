package com.margin.afd_annotation.deserialization;

import java.lang.reflect.Type;

/**
 * Created by : mr.lu
 * Created at : 2020/8/6 at 15:15
 * Description:
 */
public class Deserialization {
    public static void main(String[] args) {
        Type type = new TypeRefrence<BaseResponse>() {
        }.getType();

        System.out.println(type);
    }
} 