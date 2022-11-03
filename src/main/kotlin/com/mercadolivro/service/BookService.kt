package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.awt.print.Book

@Service
class BookService (
	private val bookRepository: BookRepository
){
	
	fun create(book: BookModel) {
		bookRepository.save(book)
	}
	
	fun getAll(pageable: Pageable): Page<BookModel>  {
		return bookRepository.findAll(pageable)
	}
	
	fun getActives(pageable: Pageable): Page<BookModel> {
		return bookRepository.findByStatus(BookStatus.ATIVO, pageable)
	}
	
	fun getById(id: Int): BookModel {
		return bookRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML101.message.format(id), Errors.ML101.code) }
	}
	
	fun getByCustomer(customer: CustomerModel): List<BookModel> {
		return bookRepository.findByCustomer(customer)
	}
	
	fun getByStatus(status: BookStatus, pageable: Pageable): Page<BookModel> {
		return bookRepository.findByStatus(status, pageable)
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
	
	fun getAllByIds(bookIds: Set<Int>): List<BookModel> {
		return bookRepository.findAllById(bookIds).toList()
	}
	
	fun getByCustomerAndStatus(customer: CustomerModel, status: BookStatus): List<BookModel> {
		
		val booksByCustomer = bookRepository.findByCustomer(customer)
		
		val books: ArrayList<BookModel> = arrayListOf()
		
		for(book in booksByCustomer) {
			if(book.status == status) {
				books.add(book)
			}
		}
		
		return books
	}
	
	
	fun purchase(books: MutableList<BookModel>) {
		books.map {
			
			if (it.id != null) {
				bookRepository.findById(it.id!!).orElseThrow{
					NotFoundException(Errors.ML101.message.format(it.id), Errors.ML101.code)
				}
			}
			
			if(it.status != BookStatus.ATIVO) {
				throw NotFoundException(Errors.ML301.message.format(it.id), Errors.ML301.code)
			} else {
				it.status = BookStatus.VENDIDO
				bookRepository.saveAll(books)
			}
		}
		
	}
}