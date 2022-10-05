package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService (
	val bookRepository: BookRepository
){
	
	fun create(book: BookModel) {
		bookRepository.save(book)
	}
	
	fun getAll(name: String?): List<BookModel>  {
		name?.let {
			bookRepository.findByNameContaining(it)
		}
		
		return bookRepository.findAll().toList()
	}
	
	fun getActives(): List<BookModel> {
		return bookRepository.findByStatus(BookStatus.ATIVO)
	}
	
	fun getById(id: Int): BookModel {
		return bookRepository.findById(id).orElseThrow()
	}
	
	fun delete(id: Int) {
		val book = getById(id)
		
		book.status = BookStatus.CANCELADO
		
		update(book)
	}
	
	fun update(book: BookModel) {
		bookRepository.save(book)
	}
	
	fun deleteByCustomer(customer: CustomerModel) {
		val books = bookRepository.findByCustomer(customer)
		
		for(book in books) {
			book.status = BookStatus.DELETADO
		}
		
		bookRepository.saveAll(books)
	}
}