package com.kevin.injecttest.annotation.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokeHandlerJava implements InvocationHandler {

    private Object mActivity;
    private Method mActivityMethod;

    public InvokeHandlerJava(Object mActivity, Method mActivityMethod) {
        this.mActivity = mActivity;
        this.mActivityMethod = mActivityMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return mActivityMethod.invoke(mActivity, args);
    }
}
