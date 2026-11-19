package com.savelife.blood_donation.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savelife.blood_donation.dto.RegistrationForm;
import com.savelife.blood_donation.entity.DonorProfile;
import com.savelife.blood_donation.entity.User;
import com.savelife.blood_donation.repository.UserRepository;

@Service
public class RegistrationService {
	private final UserRepository users;
	private final PasswordEncoder passwordEncoder;

	public RegistrationService(UserRepository users, PasswordEncoder passwordEncoder) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean emailAlreadyRegistered(String email) {
		return users.existsByEmailIgnoreCase(email.trim());
	}

	@Transactional
	public User registerDonor(RegistrationForm form) {
		if (form.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
			throw new IllegalArgumentException("Password must be no more than 72 bytes when encoded as UTF-8.");
		}
		String email = form.getEmail().trim().toLowerCase(Locale.ROOT);
		if (users.existsByEmailIgnoreCase(email)) {
			throw new IllegalArgumentException("An account already uses this email address.");
		}

		User user = new User();
		user.setFullName(form.getFullName().trim());
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(form.getPassword()));

		DonorProfile profile = new DonorProfile();
		profile.setUser(user);
		user.setDonorProfile(profile);
		return users.save(user);
	}
}
