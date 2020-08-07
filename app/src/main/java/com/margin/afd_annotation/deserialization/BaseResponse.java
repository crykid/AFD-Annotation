package com.margin.afd_annotation.deserialization;

import java.io.Serializable;

public class BaseResponse<T> implements Serializable {
   public int code =200;
   public String msg;
   public T data;

   public BaseResponse(T data) {
      this.data = data;
   }
}