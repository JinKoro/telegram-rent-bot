package infrastructure.bot

import com.google.inject.BindingAnnotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@BindingAnnotation
annotation class Rent
