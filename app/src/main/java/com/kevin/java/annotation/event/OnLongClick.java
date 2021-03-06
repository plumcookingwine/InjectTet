package com.kevin.java.annotation.event;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBus(eventType = View.OnLongClickListener.class, eventMethod = "setOnLongClickListener")
public @interface OnLongClick {

    int[] value();
}
