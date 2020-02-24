package com.kevin.java.annotation.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBus {

    // 事件类型（View.OnLongClickListener）
    Class<?> eventType();

    // 方法名 ```setOnClickListener()```
    String eventMethod();
}
