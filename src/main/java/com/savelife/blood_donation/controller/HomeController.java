package com.savelife.blood_donation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.savelife.blood_donation.service.AdminService;
import com.savelife.blood_donation.service.BloodRequestService;

@Controller
public class HomeController {
	private final AdminService adminService;
	private final BloodRequestService requestService;

	public HomeController(AdminService adminService, BloodRequestService requestService) {
		this.adminService = adminService;
		this.requestService = requestService;
	}

	@GetMapping("/")
	public String home(Model model) {
		var openRequests = requestService.getOpenRequests();
		model.addAttribute("donorCount", adminService.getDonorCount());
		model.addAttribute("openRequests", openRequests);
		model.addAttribute("openRequestCount", openRequests.size());
		return "home";
	}
}
