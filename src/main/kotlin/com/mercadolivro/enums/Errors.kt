package com.mercadolivro.enums

enum class Errors (val code: String, val message: String) {
	ML1101(code = "ML-0001", "Book [%s] not exists"),
	ML1201(code = "ML-0002", "Customer [%s] not exists")
}
