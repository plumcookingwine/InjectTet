package com.kevin.java;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kevin.java.annotation.InjectLayout;
import com.kevin.java.annotation.InjectView;
import com.kevin.java.annotation.event.EventBus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class InjectUtils {

    /**
     * 注入布局文件
     *
     * @param context 传入一个context，就是把activity传进来
     */
    static void injectLayout(Context context) {

        // 1. 获取当前class
        Class<?> clazz = context.getClass();
        // 2. 根据class获取class上面的InjectLayout注解
        InjectLayout annotation = clazz.getAnnotation(InjectLayout.class);
        // 判空
        if (annotation == null) {
            return;
        }
        // 3. 获取注解中的值，这里就是布局文件的id
        int layoutId = annotation.value();
        Log.i("kangf", "id === " + layoutId);
        try {
            // 4. 获取activity中的setContentView方法
            Method method = clazz.getMethod("setContentView", int.class);
            // 5. 执行setContentView方法，传入layoutId参数
            method.invoke(context, layoutId);
        } catch (Exception e) {
            Toast.makeText(context, "找不到setContentView方法", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    static void injectView(Context context) {
        // 1. 获取当前class
        Class<?> clazz = context.getClass();

        // 2. 获取activity中所有的成员变量
        Field[] declaredFields = clazz.getDeclaredFields();

        // 3. 开始遍历
        for (Field field : declaredFields) {
            field.setAccessible(true);
            // 4. 获取字段上面的InjectView注解
            InjectView annotation = field.getAnnotation(InjectView.class);
            // 5. 如果字段上面没有注解，就不用处理了
            if (annotation == null) {
                return;
            }
            int viewId = annotation.value();

            try {
                // 6. 获取 findViewById 方法
                Method findViewMethod = clazz.getMethod("findViewById", int.class);
                // 7. 执行方法，获取View
                View view = (View) findViewMethod.invoke(context, viewId);
                // 8. 把view赋值给该字段
                field.set(context, view);
            } catch (Exception e) {
                Toast.makeText(context, "没有找到findViewById方法", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }

    static void injectEvent(final Context context) {
        // 1. 开头是一样的，获取到class对象
        Class<?> clazz = context.getClass();
        // 2. 获取到所有的方法
        Method[] declaredMethods = clazz.getDeclaredMethods();
        // 3. 遍历所有的方法
        for (final Method method : declaredMethods) {
            method.setAccessible(true);
            // 获取方法上的注解, 因为一个方法上面可能会有多个注解，所以要获取所有的注解
            Annotation[] annotations = method.getAnnotations();
            // 遍历方法上面的注解
            for (Annotation annotation : annotations) {
                // 获取这个注解上面的注解类(也就是OnClick注解的class)
                Class<? extends Annotation> aClass = annotation.annotationType();
                // 根据OnClick注解的class，获取EventBus注解
                EventBus eventBus = aClass.getAnnotation(EventBus.class);
                // 判断如果有EventBus注解，才代表的是事件注解，进行处理
                if (eventBus != null) {
                    // 获取EventBus注解的值
                    Class<?> eventType = eventBus.eventType();
                    String eventMethodName = eventBus.eventMethod();
                    try {
                        // 通过反射拿到方法注解中的值(这里就是所有view的id数组)
                        Method ids = aClass.getDeclaredMethod("value");
                        int[] viewIds = (int[]) ids.invoke(annotation);
                        if (viewIds == null) {
                            return;
                        }
                        // 遍历id数组
                        for (int viewId : viewIds) {
                            // 获取view
                            Method findViewMethod = clazz.getMethod("findViewById", int.class);
                            View view = (View) findViewMethod.invoke(context, viewId);

                            // 如果有这个view，才进行处理
                            if (view != null) {
                                // 动态代理，代理事件类型，交给我们的方法来处理
                                Object proxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{eventType},
                                        new InvocationHandler() {
                                            @Override
                                            public Object invoke(Object proxy, Method oldMethod, Object[] args) throws Throwable {
                                                // 执行当前activity中的方法，参数不能少，需要跟原事件方法参数一样
                                                return method.invoke(context, args);
                                            }
                                        });
                                // 获取activity中的事件方法
                                Method activityEventMethod = view.getClass().getMethod(eventMethodName, eventType);
                                // 当这个方法执行的时候，自动执行代理方法
                                activityEventMethod.invoke(view, proxy);
                            }
                        }


                    } catch (Exception e) {
                        Toast.makeText(context, "找不到该value方法", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }


                }
            }
        }

    }
}
