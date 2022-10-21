package com.mercadolivro.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("admins")
class AdminController(
) {

  @GetMapping("/report")
  fun getAll(): String {
    return "This is a Report. Only Admin can see it"
  }

}