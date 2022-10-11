package com.mercadolivro.controller.request

import com.mercadolivro.validation.EmailAvailable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest (
    
    @field:NotEmpty(message = "Um nome deve ser informado")
    var name: String,

    @EmailAvailable()
    @field:Email(message = "E-mail deve ser válido")
    var email: String
)