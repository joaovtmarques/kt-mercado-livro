package com.mercadolivro.config

import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
@EnableWebSecurity
class SecurityConfig(
	private val customerRepository: CustomerRepository,
	private val userDetails: UserDetailsCustomService
): WebSecurityConfigurerAdapter() {
	
	private val PUBLIC_MATCHES = arrayOf<String>()
	
	private val PUBLIC_POST_MATCHES = arrayOf(
		"/customer"
	)
	
	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder())
	}
	
	override fun configure(http: HttpSecurity) {
		http.cors().and().csrf().disable()
		http.authorizeRequests()
			.antMatchers(*PUBLIC_MATCHES).permitAll()
			.antMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHES).permitAll()
			.anyRequest().authenticated()
		http.addFilter(AuthenticationFilter(authenticationManager(), customerRepository))
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	}
	
	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder? {
		return BCryptPasswordEncoder()
	}
}