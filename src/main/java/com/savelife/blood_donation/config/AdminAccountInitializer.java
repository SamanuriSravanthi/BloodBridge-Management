package com.savelife.blood_donation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.savelife.blood_donation.entity.User;
import com.savelife.blood_donation.entity.UserRole;
import com.savelife.blood_donation.repository.UserRepository;

@Component
public class AdminAccountInitializer implements ApplicationRunner {
	private final UserRepository users;
	private final PasswordEncoder passwordEncoder;
	private final String email;
	private final String password;

	public AdminAccountInitializer(
			UserRepository users,
			PasswordEncoder passwordEncoder,
			@Value("${app.admin.email:}") String email,
			@Value("${app.admin.password:}") String password) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
		this.email = email.trim();
		this.password = password;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (email.isBlank()) {
			return;
		}
		if (password.length() < 12) {
			throw new IllegalStateException("APP_ADMIN_PASSWORD must contain at least 12 characters.");
		}

		var existingUser = users.findByEmailIgnoreCase(email);
		if (existingUser.isPresent()) {
			if (existingUser.get().getRole() != UserRole.ADMIN) {
				throw new IllegalStateException("The configured admin email already belongs to a non-admin account.");
			}
			return;
		}

		User admin = new User();
		admin.setFullName("System Administrator");
		admin.setEmail(email);
		admin.setPassword(passwordEncoder.encode(password));
		admin.setRole(UserRole.ADMIN);
		users.save(admin);
	}
}
