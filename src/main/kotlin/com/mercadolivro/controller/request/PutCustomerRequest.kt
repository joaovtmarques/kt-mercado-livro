package com.mercadolivro.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PutCustomerRequest (
    
    @field:NotEmpty(message = "Um nome deve ser informado")
    var name: String,

    @field:Email(message = "O e-mail deve ser válido")
    var email: String
)