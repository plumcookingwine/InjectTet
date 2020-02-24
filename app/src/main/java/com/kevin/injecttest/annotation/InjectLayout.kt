package com.kevin.injecttest.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectLayout(val layoutId: Int)