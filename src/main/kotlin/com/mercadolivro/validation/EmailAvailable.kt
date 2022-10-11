package com.mercadolivro.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [EmailAvailableValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class EmailAvailable(
	val message: String = "Este e-mail já está em uso",
	val groups: Array<KClass<*>> = [],
	val payload: Array<KClass<out Payload>> = []
)
