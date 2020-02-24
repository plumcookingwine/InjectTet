package com.kevin.injecttest.annotation.event

import android.view.View


@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Event("setOnLongClickListener", View.OnLongClickListener::class, "onLongClick")
annotation class OnLongClick(vararg val ids: Int)