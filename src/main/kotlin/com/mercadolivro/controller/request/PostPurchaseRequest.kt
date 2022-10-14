package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostPurchaseRequest (
	
	@field:NotNull
	@field:Positive
	@JsonAlias("customer_id")
	val customerId: Int,
	
	@JsonAlias("book_ids")
	@field:NotNull
	val bookIds: Set<Int>
)
