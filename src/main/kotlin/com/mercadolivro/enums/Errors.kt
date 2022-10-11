package com.mercadolivro.enums

enum class Errors (val code: String, val message: String) {
	ML001(code = "ML-001", "Invalid request"),
	
	ML101(code = "ML-101", "Book [%s] not exists"),
	ML102(code = "ML-102", "Cannot update book with status [%s]"),
	
	ML201(code = "ML-201", "Customer [%s] not exists")
}
