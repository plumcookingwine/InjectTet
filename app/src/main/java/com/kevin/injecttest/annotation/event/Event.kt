package com.kevin.injecttest.annotation.event

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Event(
    val eventMethod: String,
    val eventType: KClass<*>,
    val overrideMethod: String
)