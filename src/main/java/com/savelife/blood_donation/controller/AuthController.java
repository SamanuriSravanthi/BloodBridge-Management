package com.savelife.blood_donation.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.savelife.blood_donation.dto.RegistrationForm;
import com.savelife.blood_donation.service.RegistrationService;

import jakarta.validation.Valid;

@Controller
public class AuthController {
	private final RegistrationService registrationService;

	public AuthController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		return "auth/register";
	}

	@PostMapping("/register")
	public String register(
			@Valid @ModelAttribute("registrationForm") RegistrationForm form,
			BindingResult bindingResult,
			Model model) {
		if (!bindingResult.hasErrors() && form.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
			bindingResult.rejectValue("password", "password.tooLong",
					"Password must be no more than 72 bytes when encoded as UTF-8.");
		}
		if (!bindingResult.hasErrors() && registrationService.emailAlreadyRegistered(form.getEmail())) {
			bindingResult.rejectValue("email", "duplicate", "An account already uses this email address.");
		}
		if (bindingResult.hasErrors()) {
			return "auth/register";
		}

		registrationService.registerDonor(form);
		return "redirect:/login?registered";
	}
}
