package com.jingcaiwang.mytestdemo.activities;

import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.google.gson.internal.Primitives;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 *   未包含任何成员变量----marker  annotation
 */

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface NoMemberAnnotation  {

    String value();

    Class clazz();
    String [] arr() default {"1","2"};

    Class cc() default void.class;

///设置默认值


}