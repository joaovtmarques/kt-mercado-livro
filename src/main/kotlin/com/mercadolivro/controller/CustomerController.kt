package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.enums.extension.toCustomerModel
import com.mercadolivro.enums.extension.toResponse
import com.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customers")
class CustomerController(
  val customerService: CustomerService
) {

  @GetMapping
  fun getAll(@RequestParam name: String?, @PageableDefault(page = 0, size = 10) pageable: Pageable): Page<CustomerResponse> {
    return customerService.getAll(name, pageable).map { it.toResponse() }
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody customer: PostCustomerRequest) {
    customerService.create(customer.toCustomerModel())
  }

  @GetMapping("/{id}")
  fun getCustomer(@PathVariable id: Int): CustomerResponse {
    return customerService.getById(id).toResponse()
  }
  
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun update(@PathVariable id: Int, @RequestBody customer: PutCustomerRequest) {
    val customerSaved = customerService.getById(id)
    customerService.update(customer.toCustomerModel(customerSaved))
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Int) {
    customerService.delete(id)
  }

}