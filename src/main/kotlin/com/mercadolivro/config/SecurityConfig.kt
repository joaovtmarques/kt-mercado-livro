package com.mercadolivro.config

import com.mercadolivro.enums.Role
import com.mercadolivro.exception.CustomAuthenticationEntryPoint
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
	private val customerRepository: CustomerRepository,
	private val userDetails: UserDetailsCustomService,
	private val jwtUtil: JwtUtil,
	private val customEntryPoint: CustomAuthenticationEntryPoint
): WebSecurityConfigurerAdapter() {
	
	private val PUBLIC_MATCHES = arrayOf<String>()
	
	private val PUBLIC_POST_MATCHES = arrayOf(
		"/customers"
	)
	
	private val ADMIN_MATCHES = arrayOf(
		"/admin/**"
	)
	
	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder())
	}
	
	override fun configure(http: HttpSecurity) {
		http.cors().and().csrf().disable()
		http.authorizeRequests()
			.antMatchers(*PUBLIC_MATCHES).permitAll()
			.antMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHES).permitAll()
			.antMatchers(*ADMIN_MATCHES).hasAuthority(Role.ADMIN.description)
			.anyRequest().authenticated()
		http.addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))
		http.addFilter(AuthorizationFilter(authenticationManager(), userDetails, jwtUtil))
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		http.exceptionHandling().authenticationEntryPoint(customEntryPoint)
	}
	
	override fun configure(web: WebSecurity) {
		web.ignoring().antMatchers(
			"/v2/api-docs",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-resources/configuration/ui",
			"/swagger-resources/configuration/security",
			"/swagger-resources/**",
			"/configuration/**",
			"/swagger-ui/**",
			"/webjars/**",
			"/csrf",
		)
		
		@Bean
		fun corsConfig(): CorsFilter {
			val source = UrlBasedCorsConfigurationSource()
			val config = CorsConfiguration()
			config.allowCredentials = true
			config.addAllowedOrigin("*")
			config.addAllowedHeader("*")
			config.addAllowedMethod("*")
			source.registerCorsConfiguration("/**", config)
			return CorsFilter(source)
		}
	}
	
	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder? {
		return BCryptPasswordEncoder()
	}
}