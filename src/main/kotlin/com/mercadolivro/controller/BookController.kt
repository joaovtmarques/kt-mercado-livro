package com.mercadolivro.controller

import com.electronwill.nightconfig.core.conversion.Path
import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.enums.extension.toBookModel
import com.mercadolivro.enums.extension.toResponse
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("books")
class BookController (
	val bookService: BookService,
	val customerService: CustomerService
) {
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody request: PostBookRequest) {
		val customer = customerService.getById(request.customerId)
		
		bookService.create(request.toBookModel(customer))
	}
	
	@GetMapping()
	fun getAll(@RequestParam name: String?): List<BookResponse> {
		return bookService.getAll(name).map { it.toResponse() }
	}
	
	@GetMapping("/active")
	fun getActives(): List<BookResponse> {
		return bookService.getActives().map{ it.toResponse() }
	}
	
	@GetMapping("/{id}")
	fun getById(@PathVariable id: Int): BookResponse {
		return bookService.getById(id).toResponse()
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun update(@PathVariable id: Int, @RequestBody book: PutBookRequest) {
		val bookSaved = bookService.getById(id)
		
		bookService.update(book.toBookModel(bookSaved))
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: Int) {
		bookService.delete(id)
	}
}