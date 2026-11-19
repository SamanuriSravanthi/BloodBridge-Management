package com.savelife.blood_donation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.savelife.blood_donation.repository.UserRepository;

@Configuration
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		AuthenticationSuccessHandler successHandler = (request, response, authentication) -> {
			String destination = authentication.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))
							? "/admin"
							: "/dashboard";
			response.sendRedirect(request.getContextPath() + destination);
		};

		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/", "/css/**", "/js/**", "/login", "/register").permitAll()
						.requestMatchers(HttpMethod.GET, "/requests").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/dashboard", "/profile", "/donations/**").hasRole("DONOR")
						.requestMatchers(HttpMethod.POST, "/requests").hasRole("DONOR")
						.requestMatchers("/requests/**").hasRole("DONOR")
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.successHandler(successHandler)
						.permitAll())
				.logout(logout -> logout
						.logoutSuccessUrl("/")
						.permitAll());

		return http.build();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository users) {
		return email -> users.findByEmailIgnoreCase(email)
				.map(user -> org.springframework.security.core.userdetails.User
						.withUsername(user.getEmail())
						.password(user.getPassword())
						.roles(user.getRole().name())
						.build())
				.orElseThrow(() -> new UsernameNotFoundException("No account found for " + email));
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
