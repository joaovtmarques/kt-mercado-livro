package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.controller.response.PageResponse
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toPageResponse
import com.mercadolivro.extension.toResponse
import com.mercadolivro.security.UserCanOnlyAccessTheirOwnResource
import com.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customers")
class CustomerController(
  private val customerService: CustomerService
) {

  @GetMapping
  @UserCanOnlyAccessTheirOwnResource
  fun getAll(@RequestParam name: String?, @PageableDefault(page = 0, size = 10) pageable: Pageable): PageResponse<CustomerResponse> {
    return customerService.getAll(name, pageable).map { it.toResponse() }.toPageResponse()
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  fun create(@RequestBody @Valid customer: PostCustomerRequest) {
    customerService.create(customer.toCustomerModel())
  }

  @GetMapping("/{id}")
  @UserCanOnlyAccessTheirOwnResource
  fun getCustomer(@PathVariable id: Int): CustomerResponse {
    return customerService.getById(id).toResponse()
  }
  
  @PutMapping("/{id}")
  @UserCanOnlyAccessTheirOwnResource
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
    val customerSaved = customerService.getById(id)
    customerService.update(customer.toCustomerModel(customerSaved))
  }
  
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  fun delete(@PathVariable id: Int) {
    customerService.delete(id)
  }
  
  @GetMapping("/{id}/books")
  fun getBooks(@RequestParam status: BookStatus?, @PathVariable id: Int, pageable: Pageable): List<BookResponse> {
    return customerService.getBooks(id, status, pageable).map { it.toResponse() }
  }

}