package com.savelife.blood_donation.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.savelife.blood_donation.dto.BloodRequestForm;
import com.savelife.blood_donation.entity.BloodGroup;
import com.savelife.blood_donation.service.BloodRequestService;

import jakarta.validation.Valid;

@Controller
public class BloodRequestController {
	private final BloodRequestService requestService;

	public BloodRequestController(BloodRequestService requestService) {
		this.requestService = requestService;
	}

	@GetMapping("/requests")
	public String listRequests(Model model) {
		model.addAttribute("requests", requestService.getOpenRequests());
		return "requests/list";
	}

	@GetMapping("/requests/new")
	public String newRequest(Model model) {
		model.addAttribute("requestForm", new BloodRequestForm());
		model.addAttribute("bloodGroups", BloodGroup.values());
		return "requests/form";
	}

	@PostMapping("/requests")
	public String createRequest(
			@AuthenticationPrincipal UserDetails principal,
			@Valid @ModelAttribute("requestForm") BloodRequestForm form,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("bloodGroups", BloodGroup.values());
			return "requests/form";
		}
		requestService.createRequest(principal.getUsername(), form);
		return "redirect:/requests?created";
	}
}
