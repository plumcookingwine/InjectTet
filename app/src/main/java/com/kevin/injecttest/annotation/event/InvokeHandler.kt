package com.kevin.injecttest.annotation.event

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * 这里的可变参数列表不正常。。。
 */
class InvokeHandler(
    private val activity: Any,
    private val activityMethod: Method
) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {

        Log.i("kangf", "activityMethod == ${activityMethod.name}")

        Log.i("kangf", "method == ${method?.name}")

        Log.i("kangf", "args.size == ${args?.size}")

        return activityMethod.invoke(activity, args)
    }
}