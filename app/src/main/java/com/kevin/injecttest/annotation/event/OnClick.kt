package com.kevin.injecttest.annotation.event

import android.view.View


@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Event("setOnClickListener", View.OnClickListener::class, "onClick")
annotation class OnClick(vararg val ids: Int)