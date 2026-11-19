package com.savelife.blood_donation.controller;

import java.time.LocalDate;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.savelife.blood_donation.dto.DonationForm;
import com.savelife.blood_donation.dto.DonorProfileForm;
import com.savelife.blood_donation.entity.BloodGroup;
import com.savelife.blood_donation.entity.DonorProfile;
import com.savelife.blood_donation.service.DonorService;

import jakarta.validation.Valid;

@Controller
public class DonorController {
	private final DonorService donorService;

	public DonorController(DonorService donorService) {
		this.donorService = donorService;
	}

	@GetMapping("/dashboard")
	public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model) {
		DonorProfile profile = donorService.getProfile(principal.getUsername());
		model.addAttribute("profile", profile);
		model.addAttribute("donations", donorService.getDonations(principal.getUsername()));
		DonationForm donationForm = new DonationForm();
		donationForm.setDonationDate(LocalDate.now());
		model.addAttribute("donationForm", donationForm);
		return "donor/dashboard";
	}

	@GetMapping("/profile")
	public String profileForm(@AuthenticationPrincipal UserDetails principal, Model model) {
		DonorProfile profile = donorService.getProfile(principal.getUsername());
		DonorProfileForm form = new DonorProfileForm();
		form.setBloodGroup(profile.getBloodGroup());
		form.setCity(profile.getCity());
		form.setPhone(profile.getPhone());
		form.setAvailable(profile.isAvailable());
		model.addAttribute("profileForm", form);
		model.addAttribute("bloodGroups", BloodGroup.values());
		return "donor/profile";
	}

	@PostMapping("/profile")
	public String updateProfile(
			@AuthenticationPrincipal UserDetails principal,
			@Valid @ModelAttribute("profileForm") DonorProfileForm form,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("bloodGroups", BloodGroup.values());
			return "donor/profile";
		}
		donorService.updateProfile(principal.getUsername(), form);
		return "redirect:/dashboard?profileUpdated";
	}

	@PostMapping("/donations")
	public String recordDonation(
			@AuthenticationPrincipal UserDetails principal,
			@Valid @ModelAttribute("donationForm") DonationForm form,
			BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			DonorProfile profile = donorService.getProfile(principal.getUsername());
			model.addAttribute("profile", profile);
			model.addAttribute("donations", donorService.getDonations(principal.getUsername()));
			return "donor/dashboard";
		}
		donorService.recordDonation(principal.getUsername(), form);
		return "redirect:/dashboard?donationRecorded";
	}
}
