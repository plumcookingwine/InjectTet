package com.kevin.injecttest

import android.content.Context
import android.view.View
import com.kevin.injecttest.annotation.InjectLayout
import com.kevin.injecttest.annotation.InjectView
import com.kevin.injecttest.annotation.event.Event
import com.kevin.injecttest.annotation.event.InvokeHandlerJava
import java.lang.reflect.Proxy

class InjectUtils {

    companion object {

        /**
         * 注入layout
         */
        fun injectLayout(context: Context) {
            val clazz = context::class.java
            val annotation = clazz.getAnnotation(InjectLayout::class.java) ?: return
            val layoutId = annotation.layoutId
            val setContentViewMethod = clazz.getMethod("setContentView", Int::class.java)
            setContentViewMethod.invoke(context, layoutId)
        }

        /**
         * 注入view
         */
        fun injectView(context: Context) {
            val clazz = context::class.java
            val fields = clazz.declaredFields
            fields.map {
                it.isAccessible = true
                val annotation = it.getAnnotation(InjectView::class.java) ?: return@map
                val viewId = annotation.viewId
                val method = clazz.getMethod("findViewById", Int::class.java)
                val view = method.invoke(context, viewId)
                it.set(context, view)
            }
        }

        /**
         * 事件注入
         */
        fun injectEvent(context: Context) {
            val clazz = context::class.java
            val methods = clazz.declaredMethods
            methods.map { method ->
                method.isAccessible = true
                val annotations = method.annotations
                annotations.map { annotation ->
                    // 判断是否是事件注解

                    // 获取注解类（@OnClick 或 @OnLongClick）
                    val annotationClass = annotation.annotationClass.javaObjectType
                    // 获取注解类上面的Event注解
                    val eventAnnotation = annotationClass.getAnnotation(Event::class.java)
                    if (eventAnnotation != null) {
                        // 事件方法名（setOnClickListener）
                        val eventMethod = eventAnnotation.eventMethod
                        // 事件类(View.OnClickListener)
                        val eventType = eventAnnotation.eventType
                        // 暂时用不到，事件重写的方法名
                        // val overrideMethod = eventAnnotation.overrideMethod

                        // 通过反射拿到所有view的id
                        val valueMethod = annotationClass.getDeclaredMethod("ids")
                        val viewIds = valueMethod.invoke(annotation) as IntArray
                        viewIds.map { viewId ->
                            val findViewByIdMethod =
                                clazz.getMethod("findViewById", Int::class.java)
                            val view = findViewByIdMethod.invoke(context, viewId) as? View
                            if (view != null) {
                                val proxy = Proxy.newProxyInstance(
                                    eventType.javaObjectType.classLoader,
                                    arrayOf(eventType.javaObjectType),
                                    InvokeHandlerJava(context, method)
                                )
                                val realEventMethod = view::class.java.getMethod(
                                    eventMethod,
                                    eventType.javaObjectType
                                )
                                realEventMethod.invoke(view, proxy)
                            }
                        }
                    }

                }

            }

        }


    }
}