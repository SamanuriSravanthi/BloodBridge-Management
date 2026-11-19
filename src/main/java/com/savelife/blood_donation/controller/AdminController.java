package com.savelife.blood_donation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.savelife.blood_donation.entity.RequestStatus;
import com.savelife.blood_donation.service.AdminService;

@Controller
public class AdminController {
	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@GetMapping("/admin")
	public String dashboard(Model model) {
		model.addAttribute("requests", adminService.getRequests());
		model.addAttribute("donorCount", adminService.getDonorCount());
		model.addAttribute("requestCount", adminService.getRequestCount());
		model.addAttribute("statuses", RequestStatus.values());
		return "admin/dashboard";
	}

	@PostMapping("/admin/requests/{id}/status")
	public String updateRequestStatus(@PathVariable Long id, @RequestParam RequestStatus status) {
		adminService.updateRequestStatus(id, status);
		return "redirect:/admin?updated";
	}
}
